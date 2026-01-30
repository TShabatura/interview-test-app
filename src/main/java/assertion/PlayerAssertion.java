package assertion;

import dto.player.request.PlayerRequestDTO;
import dto.player.response.PlayerResponseDTO;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Field;

import static util.ObjectFieldUtils.getFieldValue;

public class PlayerAssertion {

    public static void assertDeleteSuccess(Response response) {
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_NO_CONTENT,
                "Expected 200 or 204 on successful delete, but got " + statusCode);
    }

    public static void assertDeleteNonExisting(Response response) {
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == HttpStatus.SC_NOT_FOUND || statusCode == HttpStatus.SC_NO_CONTENT,
                "Expected 404 or 204 when deleting non-existing player, but got " + statusCode);
    }

    public static void assertPlayer(SoftAssert softAssert,
                                    PlayerResponseDTO actual,
                                    PlayerRequestDTO expected) {
        softAssert.assertEquals(actual.getAge(), expected.getAge(), "Age mismatch:");
        softAssert.assertEquals(actual.getGender(), expected.getGender(), "Gender mismatch:");
        softAssert.assertEquals(actual.getRole(), expected.getRole(), "Role mismatch:");
        softAssert.assertEquals(actual.getLogin(), expected.getLogin(), "Login mismatch:");
        softAssert.assertEquals(actual.getPassword(), expected.getPassword(), "Password mismatch:");
        softAssert.assertEquals(actual.getScreenName(), expected.getScreenName(), "ScreenName mismatch:");
    }

    public static void assertEqualField(
            SoftAssert softAssert,
            Object actual,
            Object expected,
            String fieldName
    ) {
        softAssert.assertEquals(
                getFieldValue(fieldName, actual),
                getFieldValue(fieldName, expected),
                fieldName + " mismatch");
    }

    public static void assertEqualExcept(
            SoftAssert softAssert,
            PlayerResponseDTO actual,
            PlayerResponseDTO expected,
            String excludedField
    ) {
        for (Field field : PlayerResponseDTO.class.getDeclaredFields()) {
            if (field.getName().equals(excludedField)) {
                continue;
            }
            softAssert.assertEquals(
                    getFieldValue(field.getName(), actual),
                    getFieldValue(field.getName(), expected),
                    field.getName() + " should be the same"
            );
        }
    }
}
