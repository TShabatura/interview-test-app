package data.player;

import data.enums.Role;
import dto.player.request.PlayerRequestDTO;
import org.testng.annotations.DataProvider;

import static util.RandomDataGenerator.*;

public class PlayerTestData {

    public static final int MIN_AGE = 17;
    public static final int MAX_AGE = 59;
    public static final int MIN_PASSWORD_LENGTH = 7;
    public static final int MAX_PASSWORD_LENGTH = 15;
    public static final String PASSWORD_WITHOUT_NUMBERS = "Password";
    public static final String PASSWORD_WITHOUT_LETTERS = "123456789";
    public static final String INVALID_GENDER = "Unknown";
    public static final String CONST_LOGIN = "USER";
    public static final String CONST_SCREEN_NAME = "USERNAME";

    public static PlayerRequestDTO getValidPlayer() {
        return new PlayerRequestBuilder().build();
    }

    public static PlayerRequestDTO getAdminPlayer() {
        return new PlayerRequestBuilder()
                .withRole(Role.ADMIN.value())
                .withLogin(Role.ADMIN.value()).build();
    }

    public static PlayerRequestDTO getUserPlayer() {
        return new PlayerRequestBuilder()
                .withLogin(Role.USER.value()).build();
    }

    public static PlayerRequestDTO getPlayerWithConstLoginAndScreenName() {
        return new PlayerRequestBuilder()
                .withLogin(CONST_LOGIN)
                .withScreenName(CONST_SCREEN_NAME)
                .build();
    }

    @DataProvider(name = "validPlayers")
    public static Object[][] getValidPlayers() {
        return new Object[][]{
                {new PlayerRequestBuilder().withAge(MIN_AGE).build()},
                {new PlayerRequestBuilder().withAge(MAX_AGE).build()},
                {new PlayerRequestBuilder().withPassword(getMinLengthPassword()).build()},
                {new PlayerRequestBuilder().withPassword(getMaxLengthPassword()).build()},
                {new PlayerRequestBuilder().withRole(Role.ADMIN.value()).build()},
                {new PlayerRequestBuilder().withoutPassword().build()},
        };
    }

    @DataProvider(name = "adminRoles")
    public static Object[][] getAdminRoles() {
        return new Object[][]{
                {Role.ADMIN},
                {Role.SUPERVISOR},
        };
    }

    @DataProvider(name = "invalidPlayers")
    public static Object[][] getInvalidPlayers() {
        return new Object[][]{
                {"age < min", new PlayerRequestBuilder().withAge(MIN_AGE - 1).build()},
                {"age > max", new PlayerRequestBuilder().withAge(MAX_AGE + 1).build()},
                {"pwd no digits", new PlayerRequestBuilder().withPassword(PASSWORD_WITHOUT_NUMBERS).build()},
                {"pwd no letters", new PlayerRequestBuilder().withPassword(PASSWORD_WITHOUT_LETTERS).build()},
                {"pwd too short", new PlayerRequestBuilder().withPassword(getRandomPassword(MIN_PASSWORD_LENGTH - 1)).build()},
                {"pwd too long", new PlayerRequestBuilder().withPassword(getRandomPassword(MAX_PASSWORD_LENGTH + 1)).build()},
                {"invalid gender", new PlayerRequestBuilder().withGender(INVALID_GENDER).build()},
                {"missing age", new PlayerRequestBuilder().withoutAge().build()},
                {"missing gender", new PlayerRequestBuilder().withoutGender().build()},
                {"missing role", new PlayerRequestBuilder().withoutRole().build()},
                {"missing login", new PlayerRequestBuilder().withoutLogin().build()},
                {"missing screenName", new PlayerRequestBuilder().withoutScreenName().build()},
        };
    }

    @DataProvider(name = "fieldsForUpdate")
    public static Object[][] getFieldsForUpdate() {
        return new Object[][]{
                {"age"},
                {"gender"},
                {"login"},
                {"password"},
                {"screenName"},
        };
    }

    @DataProvider(name = "invalidDataForUpdate")
    public static Object[][] getInvalidDataForUpdate() {
        return new Object[][]{
                {"age < min", getEmptyPlayerRequestBuilder().withAge(MIN_AGE - 1).build()},
                {"age > max", getEmptyPlayerRequestBuilder().withAge(MAX_AGE + 1).build()},
                {"pwd no digits", getEmptyPlayerRequestBuilder().withPassword(PASSWORD_WITHOUT_NUMBERS).build()},
                {"pwd no letters", getEmptyPlayerRequestBuilder().withPassword(PASSWORD_WITHOUT_LETTERS).build()},
                {"pwd too short", getEmptyPlayerRequestBuilder().withPassword(getRandomPassword(MIN_PASSWORD_LENGTH - 1)).build()},
                {"pwd too long", getEmptyPlayerRequestBuilder().withPassword(getRandomPassword(MAX_PASSWORD_LENGTH + 1)).build()},
                {"invalid gender", getEmptyPlayerRequestBuilder().withGender(INVALID_GENDER).build()}
        };
    }

    public static PlayerRequestBuilder getEmptyPlayerRequestBuilder() {
        return new PlayerRequestBuilder()
                .withoutAge()
                .withoutGender()
                .withoutLogin()
                .withoutPassword()
                .withoutRole()
                .withoutScreenName();
    }

    public static PlayerRequestDTO getPlayerRequestWithChangedField(String fieldName) {
        PlayerRequestBuilder builder = getEmptyPlayerRequestBuilder();
        return switch (fieldName) {
            case "age" -> builder.withAge(MIN_AGE).build();
            case "gender" -> builder.withGender(getRandomGender()).build();
            case "login" -> builder.withLogin(getRandomLogin()).build();
            case "password" -> builder.withPassword(getRandomPassword(10)).build();
            case "role" -> builder.withRole(Role.ADMIN.value()).build();
            case "screenName" -> builder.withScreenName(getRandomScreenName()).build();
            default -> throw new IllegalArgumentException("Unknown field: " + fieldName);
        };
    }
}
