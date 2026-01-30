package config;

import util.PropertyReader;

public class ConfigManager {

    private static final String CONFIG_FILE = "config.properties";

    private ConfigManager() {
    }

    private static class Holder {
        private static final Config INSTANCE =
                new Config(new PropertyReader(CONFIG_FILE));
    }

    public static Config getConfig() {
        return Holder.INSTANCE;
    }
}
