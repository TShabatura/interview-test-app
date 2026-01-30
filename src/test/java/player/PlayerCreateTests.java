package player;

import data.enums.Role;
import data.player.PlayerTestData;
import dto.player.request.PlayerRequestDTO;
import dto.player.response.PlayerResponseDTO;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import util.AllureUtils;

import static assertion.PlayerAssertion.assertPlayer;
import static data.player.PlayerTestData.getPlayerWithConstLoginAndScreenName;
import static data.player.PlayerTestData.getValidPlayer;

@Feature("Create Player")
public class PlayerCreateTests extends BaseTest {

    @Issue("BUG-1")
    @Description("Response returns null values")
    @Test(
            description = "Create player with valid data — expect populated DTO and 200",
            dataProvider = "validPlayers", dataProviderClass = PlayerTestData.class
    )
    public void createPlayerWithValidRequest(PlayerRequestDTO requestedPlayer) {
        AllureUtils.attachDto("requestedPlayer", requestedPlayer);

        PlayerResponseDTO createdPlayer = playerService.createPlayer(requestedPlayer, Role.SUPERVISOR)
                .then()
                .statusCode(200)
                .extract()
                .as(PlayerResponseDTO.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(createdPlayer.getId());
        assertPlayer(softAssert, createdPlayer, requestedPlayer);
        softAssert.assertAll();
    }

    @Test(description = "Create player as ADMIN — expect 200", groups = "requiresAdmin")
    public void createPlayerAsAdmin() {
        PlayerRequestDTO player = getValidPlayer();
        playerService.createPlayer(player, Role.ADMIN)
                .then()
                .statusCode(200);
    }

    @Issue("BUG-2")
    @Description("Player with invalid data is created after sending request with invalid data. 400 should be returned")
    @Test(
            description = "Create player with invalid data — expect 400",
            dataProvider = "invalidPlayers", dataProviderClass = PlayerTestData.class
    )
    public void createPlayerWithInvalidRequest(String caseName, PlayerRequestDTO player) {
        Allure.parameter("Test case", caseName);

        playerService.createPlayer(player, Role.SUPERVISOR)
                .then()
                .statusCode(400);
    }

    @Test(description = "Create player as USER — expect 403 (forbidden)", groups = "requiresUser")
    public void createPlayerAsUser() {
        PlayerRequestDTO player = getValidPlayer();

        playerService.createPlayer(player, Role.USER)
                .then()
                .statusCode(403);
    }

    @Issue("BUG-1")
    @Description("Response returns null values")
    @Test(description = "Create player with existing login and screenName — expect existing player returned (200)")
    public void createPlayerWithExistingUserData() {
        PlayerRequestDTO player = getPlayerWithConstLoginAndScreenName();

        playerService.createPlayer(player, Role.SUPERVISOR)
                .then().statusCode(200);

        PlayerRequestDTO expectedPlayer = getPlayerWithConstLoginAndScreenName();
        PlayerResponseDTO actualPlayer = playerService.createPlayer(player, Role.SUPERVISOR)
                .then().statusCode(200)
                .extract()
                .as(PlayerResponseDTO.class);

        SoftAssert softAssert = new SoftAssert();
        assertPlayer(softAssert, actualPlayer, expectedPlayer);
        softAssert.assertAll();
    }

    @Issue("BUG-3")
    @Description("Response returns 403, but 401 is expected, as Admin player doesn't exist and cannot be authorized")
    @Test(description = "Create player as non-existing ADMIN — expect 401 (unauthorized)")
    public void createPlayerAsNonExistingAdmin() {
        PlayerRequestDTO player = getValidPlayer();

        playerService.createPlayer(player, Role.ADMIN)
                .then().statusCode(401);
    }
}
