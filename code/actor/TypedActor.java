/*
 *    Inspired by Viktor Klang's minscalaactors.scala
 *    https://gist.github.com/viktorklang/2362563
 *    Copyright 2014 Viktor Klang
 *
 *    Copyright 2021 Edoardo Vacchi
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

//JAVA 19
//JAVAC_OPTIONS --enable-preview --release 19
//JAVA_OPTIONS  --enable-preview

package io.github.evacchi;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import static java.lang.System.out;

public interface TypedActor {
    interface Effect<T> extends Function<Behavior<T>, Behavior<T>> {}
    interface Behavior<T> extends Function<T, Effect<T>> {}
    interface Address<T> { Address<T> tell(T msg); }
    static <T> Effect<T> Become(Behavior<T> next) { return current -> next; }
    static <T> Effect<T> Stay() { return current -> current; }
    static <T> Effect<T> Die() { return Become(msg -> { out.println("Dropping msg [" + msg + "] due to severe case of death."); return Stay(); }); }


    class AtomicRunnableAddress<T> implements Address<T>, Runnable {
        final AtomicInteger on = new AtomicInteger(0);
        final Executor executor;
        Behavior<T> behavior;

        AtomicRunnableAddress(Executor executor, Function<Address<T>, Behavior<T>> initial) {
            this.executor = executor;
            this.behavior = initial.apply(this);
        }

        // Our awesome little mailbox, free of blocking and evil
        final ConcurrentLinkedQueue<T> mbox = new ConcurrentLinkedQueue<>();
        
        public Address<T> tell(T msg) { mbox.offer(msg); async(); return this; }  // Enqueue the message onto the mailbox and try to schedule for execution
        // Switch ourselves off, and then see if we should be rescheduled for execution
        public void run() {
            try { if (on.get() == 1) { T m = mbox.poll(); if (m != null) behavior = behavior.apply(m).apply(behavior); }
            } finally { on.set(0); async(); }
        }
        // If there's something to process, and we're not already scheduled
        void async() {
            if (!mbox.isEmpty() && on.compareAndSet(0, 1)) {
                // Schedule to run on the Executor and back out on failure
                try { executor.execute(this); } catch (Throwable t) {
                    on.set(0);
                    throw t;
                }
            }
        }
        
    }

    record System(Executor executor) {
        public <T> Address<T> actorOf(Function<Address<T>, Behavior<T>> initial) {
            return new AtomicRunnableAddress<T>(executor, initial);
        }
    }

}