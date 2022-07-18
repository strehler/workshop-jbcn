import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import static java.lang.System.out;

public interface Actor {
    interface Behavior extends Function<Object, Effect> {}
    interface Effect extends Function<Behavior, Behavior> {}
    interface Address { Address tell(Object msg); }
}
