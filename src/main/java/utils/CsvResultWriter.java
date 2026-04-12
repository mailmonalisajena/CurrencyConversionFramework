package utils;

import com.opencsv.CSVWriter;
import utils.Publicdata;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvResultWriter {

    private static final String OUTPUT_DIR = "test-output";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    private static final DateTimeFormatter FORMATTER =
//            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
//    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
//            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Creates a new CSV file with execution results.
     */
    public static String writeResults(String inputFilePath,
                                      List<Publicdata> dataList) {

        try {
            // Create output directory if it doesn't exist
            File dir = new File(OUTPUT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Generate result file name based on input file
            String inputFileName = new File(inputFilePath).getName()
                    .replace(".csv", "");
            String timestamp = LocalDateTime.now().format(FORMATTER);

            String outputFilePath = OUTPUT_DIR + File.separator +
                    inputFileName + "_results_" + timestamp + ".csv";

            try (CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {

                // Write header
                writer.writeNext(new String[]{
                        "fromCurrency",
                        "toCurrency",
                        "amount",
                        "expectedValue",
                        "tolerance",
                        "actualValue",
                        "upperLimit",
                        "lowerLimit",
                        "status"
                });

                // Write data rows
                for (Publicdata data : dataList) {
                    writer.writeNext(new String[]{
                            data.fromCurrency,
                            data.toCurrency,
                            data.amount,
                            data.testValue,
                            data.tolerance,
                            data.actualValue,
                            data.upperLimit,
                            data.lowerLimit,
                            data.status
                    });
                }
            }

            System.out.println("The Output result stored into CSV at: " + outputFilePath);
            return outputFilePath;

        } catch (Exception e) {
            throw new RuntimeException("Failed to write result CSV", e);
        }
    }

    /**
     * Returns the current timestamp for each test execution.
     */
    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }
}