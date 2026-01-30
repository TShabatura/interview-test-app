package util;

import data.enums.Gender;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

import static data.player.PlayerTestData.MAX_PASSWORD_LENGTH;
import static data.player.PlayerTestData.MIN_PASSWORD_LENGTH;

public class RandomDataGenerator {

    public static final String ALPHANUMERIC_PASSWORD_REGEX = "(?=.*[A-Za-z])(?=.*\\d).*";

    public static String getRandomAlphanumericString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String getRandomScreenName() {
        return Thread.currentThread().getName() + "-" + getRandomAlphanumericString(3);
    }

    public static String getRandomLogin() {
        return getRandomAlphanumericString(10);
    }

    public static String getRandomPassword(int length) {
        String password;
        do {
            password = getRandomAlphanumericString(length);
        } while (!password.matches(ALPHANUMERIC_PASSWORD_REGEX));
        return password;
    }

    public static String getMinLengthPassword() {
        return getRandomPassword(MIN_PASSWORD_LENGTH);
    }

    public static String getMaxLengthPassword() {
        return getRandomPassword(MAX_PASSWORD_LENGTH);
    }

    public static <E extends Enum<E>> E getRandomEnum(Class<E> enumClass) {
        E[] values = enumClass.getEnumConstants();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    public static String getRandomGender() {
        return getRandomEnum(Gender.class).value();
    }
}
