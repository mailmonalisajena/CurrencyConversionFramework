package tests;

import utils.CsvReader;
import utils.Publicdata;
import base.BaseTest;
import pages.CurrencyPage;
import utils.ConfigReader;

import java.util.List;

public class CurrencyTest extends BaseTest {

    public static void main(String[] args) {

        var config = ConfigReader.get();
        String filePath = config.testDataPath;
        List<Publicdata> dataList;

        if (filePath.toLowerCase().endsWith(".csv")) {
            dataList = CsvReader.read(filePath);
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file type. Please provide CSV or JSON."
            );
        }

        CurrencyTest test = new CurrencyTest();
        //test.setUp();

        TestExecutor executor = new TestExecutor(dataList, filePath);
        executor.run();

        test.tearDown();

        System.out.println("Framework execution completed.");
    }
}
