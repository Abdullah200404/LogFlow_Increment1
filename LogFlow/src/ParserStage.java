import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserStageTest {

    private ParserStage parserStage;
    private List<Object> emittedRecords;
    private Emitter testEmitter;

    @BeforeEach
    void setUp() {
        parserStage = new ParserStage();
        emittedRecords = new ArrayList<>();
        // Test Double / Collecting Emitter
        testEmitter = emittedRecords::add;
    }

    @Test
    void testValidLogLine() {
        String line = "192.168.2.11 GET / 200";
        parserStage.process(line, testEmitter);

        assertEquals(1, emittedRecords.size());
        assertEquals(1, parserStage.getTotalLines());
        assertEquals(0, parserStage.getErrorLines());
    }

    @Test
    void testMissingFieldLine() {
        String line = "192.168.2.11 GET";
        parserStage.process(line, testEmitter);

        assertEquals(0, emittedRecords.size());
        assertEquals(1, parserStage.getErrorLines());
    }

    @Test
    void testInvalidTimestamp() {
        String line = "INVALID_LOG_STRING";
        parserStage.process(line, testEmitter);

        assertEquals(0, emittedRecords.size());
        assertEquals(1, parserStage.getErrorLines());
    }

    @Test
    void testInvalidStatusCode() {
        String line = "192.168.2.11 GET / ABC";
        parserStage.process(line, testEmitter);

        assertEquals(0, emittedRecords.size());
        assertEquals(1, parserStage.getErrorLines());
    }

    @Test
    void testEmptyLine() {
        parserStage.process("   ", testEmitter);

        assertEquals(0, emittedRecords.size());
        assertEquals(1, parserStage.getErrorLines());
    }

    @Test
    void testExtraSpaces() {
        String line = "   192.168.2.11   GET   /index.html   200   ";
        parserStage.process(line, testEmitter);

        assertEquals(1, emittedRecords.size());
        assertEquals(0, parserStage.getErrorLines());
    }

    @Test
    void testUserAgentWithQuotes() {
        String line = "192.168.2.11 GET /login 200";
        parserStage.process(line, testEmitter);

        assertEquals(1, emittedRecords.size());
    }

    @Test
    void testQueryStringInUrl() {
        String line = "192.168.2.11 GET /search?q=java 200";
        parserStage.process(line, testEmitter);

        assertEquals(1, emittedRecords.size());
    }
}