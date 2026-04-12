package utils;

import utils.Publicdata;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class LoggerUtil {

    private static final String OUTPUT_DIR = "test-output";
    private static final String LOG_FILE = OUTPUT_DIR + "/execution.log";

    static {
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static synchronized void log(Publicdata data) {

        String logMessage = String.format(
                "%s -> %s | Amount: %s | TestValue: %s | Actual: %s | LowerLimit: %s | UpperLimit: %s |Status: %s",
                data.fromCurrency,
                data.toCurrency,
                data.amount,
                data.testValue,
                data.actualValue,
                data.lowerLimit,
                data.upperLimit,
                data.status
        );

        System.out.println(logMessage);

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(logMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}