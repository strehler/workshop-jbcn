///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 19
//JAVAC_OPTIONS --enable-preview --release 19
//JAVA_OPTIONS  --enable-preview

import static java.lang.System.*;

public class pattern {

    public static void main(String... args) {
        switch (Integer.valueOf(args.length)) {
            case Integer i when i > 0 -> out.println("Hello " + args[0]);
            default -> out.println("Hello World");
        }
    }
}
