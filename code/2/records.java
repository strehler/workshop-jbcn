///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 19
//JAVAC_OPTIONS --enable-preview --release 19
//JAVA_OPTIONS  --enable-preview

import static java.lang.System.*;

public class records {
    record User(String name) {};

    public static void main(String... args) {
        var user = new User(args.length > 0 ? args[0] : "World");
        out.println("Hello " + user.name());
    }
}
