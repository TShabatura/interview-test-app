package util;

import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private final Properties properties = new Properties();

    public PropertyReader(String propertyFile) {
        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(propertyFile)) {

            if (is == null) {
                throw new IllegalStateException(
                        "Property file not found: " + propertyFile);
            }

            properties.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties", e);
        }
    }

    public String get(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }

    public String get(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }
}
