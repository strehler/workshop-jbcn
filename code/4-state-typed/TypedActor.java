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
}
