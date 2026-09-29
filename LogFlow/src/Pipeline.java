import java.util.ArrayList;
import java.util.List;

public class Pipeline<I, O> {
    private final Source<I> source;
    private final Sink<O> sink;
    private final List<Stage<?, ?>> stages = new ArrayList<>();

    public Pipeline(Source<I> source, Sink<O> sink) {
        this.source = source;
        this.sink = sink;
    }

    @SuppressWarnings("unchecked")
    public void run() throws Exception {
        // في هذه المرحلة الأولى، نمرر البيانات مباشرة من المصدر إلى المخرج
        Emitter<I> emitter = item -> ((Sink<I>) sink).consume(item);
        source.produce(emitter);
    }
}