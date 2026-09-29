public interface Stage<I, O> {
    void process(I input, Emitter<O> out) throws Exception;

    default void open() {
        // Lifecycle hook, unused this week
    }

    default void close() {
        // Lifecycle hook, unused this week
    }
}