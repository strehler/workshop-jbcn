
public interface TypedActor {
    interface Effect<T> { Behavior<T> transition(Behavior<T> next); }
    interface Behavior<T> { Effect<T> receive(T o); }
    interface Address<T> { Address<T> tell(T msg); }
}
