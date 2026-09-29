public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <file-path>");
            return;
        }
        String filePath = args[0];


        Source<String> source = new FileLineSource(filePath);
        Sink<String> sink = new ConsoleSink();


        Pipeline<String, String> pipeline = new Pipeline<>(source, sink);


        try {
            pipeline.run();
        } catch (Exception e) {
            System.err.println("Error running pipeline: " + e.getMessage());
            e.printStackTrace();
        }
    }
}