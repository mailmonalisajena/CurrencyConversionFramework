package utils;

import utils.ConfigReader;
import utils.Publicdata;

public class ToleranceService {


    public static Publicdata validate(Publicdata data) {
        var config = ConfigReader.get();
        double defaultToleranceValue;
        defaultToleranceValue = config.defaultTolerance;
        double toleranceValue;

        if (data.tolerance == null || data.tolerance.isEmpty()){
            //data.defaultTolerance = String.valueOf(defaultToleranceValue);
            data.tolerance = String.valueOf(defaultToleranceValue);
            toleranceValue = Double.parseDouble(data.actualValue) * (defaultToleranceValue / 100);

        }
        else{

            toleranceValue = Double.parseDouble(data.actualValue) * (Double.parseDouble(data.tolerance) / 100);
        }

        double lowerLimit = Double.parseDouble(data.actualValue) - toleranceValue;
        data.lowerLimit = String.valueOf(lowerLimit);
        double upperLimit = Double.parseDouble(data.actualValue) + toleranceValue;
        data.upperLimit=String.valueOf(upperLimit);
        boolean isPass = Double.parseDouble(data.testValue) >= lowerLimit &&
                Double.parseDouble(data.testValue) <= upperLimit;
        data.status = isPass ? "PASS" : "FAIL";
        return data;
    }
}