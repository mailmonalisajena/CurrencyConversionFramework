package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class ConfigReader {
    private static Publicdata config;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(
                    new File("src/test/test-input/config.json"),
                    Publicdata.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Publicdata get() {
        return config;
    }
}
