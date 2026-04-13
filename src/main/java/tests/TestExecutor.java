package tests;

import pages.CurrencyPage;
import utils.CsvResultWriter;
import utils.LoggerUtil;
import utils.Publicdata;
import base.BaseTest;
import com.microsoft.playwright.Page;
import utils.ToleranceService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestExecutor {

    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final List<Publicdata> dataList;
    private final String outputFilePath;

    public TestExecutor(List<Publicdata> dataList, String outputFilePath) {
        this.dataList = dataList;
        this.outputFilePath = outputFilePath;
    }

    public void run() {

        for (Publicdata data : dataList) {
            executor.submit(() -> execute(data));
        }
        executor.shutdown();

        try {
            if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Generate result file based on input type
        if (outputFilePath.toLowerCase().endsWith(".csv")) {
            CsvResultWriter.writeResults(outputFilePath, dataList);
        }

        System.out.println("Execution completed successfully.");
    }

    /**
     * Validates the currency page and get the actual exchange value.
     */
    private void execute(Publicdata data) {

        double actualValue=0;

        try {
            Page page = BaseTest.getPage();
            CurrencyPage currencyPage = new CurrencyPage(page);
            actualValue = currencyPage.getConvertedValue(data.fromCurrency,
                    data.toCurrency,
                    data.amount);

            data.actualValue = String.valueOf(actualValue);

            ToleranceService.validate(data);
            //page.close();
            LoggerUtil.log(data);


        } catch (Exception e) {
            data.actualValue=String.valueOf(actualValue);
            data.status = "ERROR";
            LoggerUtil.log(data);
            e.printStackTrace();
        }
    }
}
