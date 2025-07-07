import java.io.*;
import java.util.*;

public class CsvComparator {

    /**
     * Compares two CSV files and returns a list of differences indicating
     * whether they are the same or different by row and column.
     * 
     * @param file1 The path to the first CSV file.
     * @param file2 The path to the second CSV file.
     * @return A list of messages indicating the result of the comparison.
     */
    public List<String> compare(String file1, String file2) {
        List<String> differences = new ArrayList<>();

        try (
            BufferedReader br1 = new BufferedReader(new FileReader(file1));
            BufferedReader br2 = new BufferedReader(new FileReader(file2));
        ) {
            // Read the files line by line and compare each column
            String line1 = br1.readLine();
            String line2 = br2.readLine();
            int rowNum = 1;

            // Files are different lengths
            while (line1 != null || line2 != null) {
                // Compare current lines and collect any differences
                differences.addAll(compareLines(line1, line2, rowNum, file1, file2));

                // Read next lines
                line1 = br1.readLine();
                line2 = br2.readLine();
                rowNum++;
            }

            // After loop ends, check if files differ in length
            if (br1.readLine() != null) {
                differences.add(String.format("%s is longer than %s.", file1, file2));
            } else if (br2.readLine() != null) {
                differences.add(String.format("%s is longer than %s.", file2, file1));
            }

        } catch (FileNotFoundException e) {
            differences.add("One of the files does not exist.");
        } catch (IOException e) {
            differences.add("An error occurred while reading the files.");
        }

        return differences;
    }

    /**
     * Compares two lines of CSV data and returns a list of differences
     * for that specific row and its columns.
     */
    private List<String> compareLines(String line1, String line2, int rowNum, String file1, String file2) {
        List<String> result = new ArrayList<>();

        if (line1 == null) {
            result.add(String.format("Row %d mismatch found in %s: this row is empty, but %s has data.", rowNum, file1, file2));
            line1 = "";
        }
        if (line2 == null) {
            result.add(String.format("Row %d mismatch found in %s: this row is empty, but %s has data.", rowNum, file2, file1));
            line2 = "";
        }

        String[] columns1 = line1.split(",", -1);
        String[] columns2 = line2.split(",", -1);
        int maxCols = Math.max(columns1.length, columns2.length);

        // Compare columns
        for (int col = 0; col < maxCols; col++) {
            String value1 = (col < columns1.length) ? columns1[col] : "";
            String value2 = (col < columns2.length) ? columns2[col] : "";

            // Print filename and column difference
            if (!value1.equals(value2)) {
                result.add(String.format(
                    "Difference at row %d, column %d:\n%s: \"%s\"\n%s: \"%s\"",
                    rowNum, col + 1, file1, value1, file2, value2
                ));
            }
        }

        return result;
    }
}
