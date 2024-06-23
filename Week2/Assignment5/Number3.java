import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Number3 {

    public static void main(String[] args) {
        String inputFilePath = "input.csv"; // Change this to your input file path
        String outputFilePath = "output.csv"; // Change this to your output file path
        String keyField = "id"; // Change this to your key field name for CSV
        int keyFieldPosition = 0; // Change this to your key field position for TXT (0-based index)
        boolean isCsv = inputFilePath.endsWith(".csv");

        try {
            if (isCsv) {
                removeDuplicatesFromCsv(inputFilePath, outputFilePath, keyField);
            } else {
                removeDuplicatesFromTxt(inputFilePath, outputFilePath, keyFieldPosition);
            }
            System.out.println("Duplicates removed and output written to: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeDuplicatesFromCsv(String inputFilePath, String outputFilePath, String keyField) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath));
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {

            String header = reader.readLine();
            writer.write(header + "\n");

            String[] headers = header.split(",");
            int keyFieldIndex = Arrays.asList(headers).indexOf(keyField);
            if (keyFieldIndex == -1) {
                throw new IllegalArgumentException("Key field not found in the CSV header");
            }

            Set<String> seenKeys = new HashSet<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                String keyValue = values[keyFieldIndex];

                if (!seenKeys.contains(keyValue)) {
                    seenKeys.add(keyValue);
                    writer.write(line + "\n");
                }
            }
        }
    }

    private static void removeDuplicatesFromTxt(String inputFilePath, String outputFilePath, int keyFieldPosition) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath));
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {

            Set<String> seenKeys = new HashSet<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\\s+");
                String keyValue = values[keyFieldPosition];

                if (!seenKeys.contains(keyValue)) {
                    seenKeys.add(keyValue);
                    writer.write(line + "\n");
                }
            }
        }
    }
}
