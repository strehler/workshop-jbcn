///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 19
//JAVAC_OPTIONS --enable-preview --release 19
//JAVA_OPTIONS  --enable-preview

import static java.lang.System.*;

public class selead {
    sealed interface ToGreet {};
    record User(String name) implements ToGreet {};
    record World() implements ToGreet {};

    public static void main(String... args) {
        var toGreet = args.length > 0 ? new User(args[0]) : new World();
        out.println("Hello " + toGreet);
    }
}
