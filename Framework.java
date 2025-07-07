import java.util.*;

public class Framework {
    public static void main(String[] args) {
        boolean summaryOnly = false;
        List<String> fileArgs = new ArrayList<>();

        // Parse command-line arguments
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--summary")) {
                summaryOnly = true;
            } else {
                fileArgs.add(arg);
            }
        }

        // Check if user provided exactly two file names
        if (fileArgs.size() != 2) {
            System.out.println("Usage: [--summary] <file1.csv> <file2.csv>");
            return;
        }

        // Get the file names
        String file1 = fileArgs.get(0);
        String file2 = fileArgs.get(1);

        // Create and use CsvComparator instance
        CsvComparator comparator = new CsvComparator();
        List<String> results = comparator.compare(file1, file2);

        // Print results
        if (summaryOnly) {
            System.out.println(results.isEmpty() ? "Files are identical." : "Files differ.");
        } else {
            if (results.isEmpty()) {
                System.out.println(file1 + " and " + file2 + " are identical.");
            } else {
                System.out.println("Differences found:");
                for (String line : results) {
                    System.out.println(line);
                }
            }
        }
    }
}
