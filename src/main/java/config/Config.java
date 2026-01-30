package config;

import util.PropertyReader;

public class Config {

    private final String baseUrl;

    public Config(PropertyReader propertyReader) {
        this.baseUrl = propertyReader.get("base.url");

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("base.url is not defined");
        }
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }
}
