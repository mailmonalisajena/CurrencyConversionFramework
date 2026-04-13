package utils;

import com.opencsv.CSVReader;
import utils.Publicdata;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static List<Publicdata> read(String path) {
        List<Publicdata> list = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(path))) {

            String[] line;
            int rowNumber = 1;

            reader.readNext(); // Skip header row

            while ((line = reader.readNext()) != null) {
                rowNumber++;

                try {
                    validateRow(line, rowNumber);

                    Publicdata data = new Publicdata();
                    data.fromCurrency = line[0].trim();
                    data.toCurrency   = line[1].trim();
                    data.amount       = line[2].trim();
                    data.testValue    = line[3].trim();
                    data.tolerance    = line[4].trim();

                    list.add(data);

                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping row " + rowNumber + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file: " + path, e);
        }

        return list;
    }

    /**
     * Validates that all required fields are present and not empty.
     */
    private static void validateRow(String[] line, int rowNumber) {
        if (line.length < 5) {
            throw new IllegalArgumentException(
                    "Row " + rowNumber + " does not contain all required columns."
            );
        }

        String[] fieldNames = {
                "From Currency",
                "To Currency",
                "Amount",
                "Expected Value"
        };

        for (int i = 0; i < 4; i++) {
            if (line[i] == null || line[i].trim().isEmpty()) {
                throw new IllegalArgumentException(
                        fieldNames[i] + " is empty."
                );
            }
        }
    }
}