package pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import utils.ConfigReader;

import java.util.Currency;
import java.util.regex.Pattern;

public class CurrencyPage {

    Page page;
    public CurrencyPage(Page page) {
        this.page = page;
    }

    public double getConvertedValue(String fromCurrency, String toCurrency, String amount) {

        page.navigate(ConfigReader.get().baseUrl);
        page.waitForTimeout(5000);

        //Validate Currency before execution
        currencyCheck(fromCurrency);
        currencyCheck(toCurrency);

        // Before filling amount Validate its containing only digits.
        if (!amount.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "Invalid amount entered. Expected digits only, but found: " + amount
            );
        }
        Locator amountField = page.getByLabel("Amount");
        amountField.clear();
        amountField.fill(amount);


        String fromDefaultCurrency = page.locator("#midmarketFromCurrency").textContent();
        String toDefaultCurrency = page.locator("#midmarketToCurrency").textContent();
        // fill from currency
        if (!fromDefaultCurrency.contains(fromCurrency)) {
            page.locator("#midmarketFromCurrency").locator("input").fill(fromCurrency);
            page.getByText(Pattern.compile("^" + fromCurrency + "\\s.*", Pattern.CASE_INSENSITIVE)).click();

        }

        //fill toCurrency
        if (!toDefaultCurrency.contains(toCurrency)) {
            page.locator("#midmarketToCurrency").locator("input").fill(toCurrency);
            page.getByText(Pattern.compile("^" + toCurrency + "\\s.*", Pattern.CASE_INSENSITIVE)).click();
        }

        // To Convert the amount
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Convert")).click();

        // To collect the converted amount
        Locator toAmount = page.getByLabel("Receiving amount").nth(1);
        String actualValueFromXE = toAmount.inputValue();

        // Removed commas before passing to double
        actualValueFromXE = actualValueFromXE.replace(",", "");

        page.close();

        return Double.parseDouble(actualValueFromXE);

    }

    public static void currencyCheck(String currency) {
        try {
            Currency.getInstance(currency);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Currency entered, Given currency: " + currency
            );
        }
    }


}