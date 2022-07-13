///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 19
//JAVAC_OPTIONS --enable-preview --release 19
//JAVA_OPTIONS  --enable-preview

import static java.lang.System.*;

public class hello {
    record User(String name) {};

    public static void main(String... args) {
        switch(new User(args.length > 0 ? args[0] : "World")) {
            case User(var name) -> out.println("Hello " + name);
        }
    }
}
