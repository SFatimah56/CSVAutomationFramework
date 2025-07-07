import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CsvComparatorTest {
    CsvComparator comparator;

    @BeforeEach
    void setup() {
        comparator = new CsvComparator();
    }

    // Utility to create a temp CSV file with given content
    private File createTempCsv(String content) throws IOException {
        File tempFile = File.createTempFile("test_", ".csv");
        Files.write(tempFile.toPath(), content.getBytes());
        tempFile.deleteOnExit();
        return tempFile;
    }

    @Test
    void testIdenticalFiles() throws IOException {
        String content = "name,age\nAlice,30\nBob,25";
        File file1 = createTempCsv(content);
        File file2 = createTempCsv(content);

        List<String> result = comparator.compare(file1.getPath(), file2.getPath());

        assertTrue(result.isEmpty(), "Expected no differences for identical files");
    }

    @Test
    void testDifferentValues() throws IOException {
        File file1 = createTempCsv("name,age\nAlice,30\nBob,25");
        File file2 = createTempCsv("name,age\nAlice,31\nBob,25");

        List<String> result = comparator.compare(file1.getPath(), file2.getPath());

        assertFalse(result.isEmpty(), "Expected differences");
        assertTrue(result.get(0).contains("row 2"), "Should report difference on row 2");
    }

    @Test
    void testDifferentLengths() throws IOException {
        File file1 = createTempCsv("name,age\nAlice,30");
        File file2 = createTempCsv("name,age\nAlice,30\nBob,25");

        List<String> result = comparator.compare(file1.getPath(), file2.getPath());

        assertFalse(result.isEmpty(), "Expected differences for different length files");

        // Check that one of the messages indicates a missing row
        boolean hasMissingRowMessage = result.stream()
            .anyMatch(s -> s.contains("this row is empty"));

        assertTrue(hasMissingRowMessage, "Should report missing row difference");
    }

    @Test
    void testDifferentColumnCount() throws IOException {
        File file1 = createTempCsv("name,age\nAlice,30");
        File file2 = createTempCsv("name\nAlice");

        List<String> result = comparator.compare(file1.getPath(), file2.getPath());

        assertFalse(result.isEmpty());
        assertTrue(result.get(0).contains("column 2"), "Should report column mismatch");
    }

    @Test
    void testEmptyFiles() throws IOException {
        File file1 = createTempCsv("");
        File file2 = createTempCsv("");

        List<String> result = comparator.compare(file1.getPath(), file2.getPath());

        assertTrue(result.isEmpty(), "Empty files should be considered identical");
    }
}
