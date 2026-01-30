package player;

import data.enums.Role;
import dto.PlayerIdDTO;
import dto.player.request.PlayerRequestDTO;
import dto.player.response.PlayerResponseDTO;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import session.Session;

import static assertion.PlayerAssertion.assertPlayer;
import static session.SessionKey.CREATED_PLAYER;
import static session.SessionKey.CREATED_PLAYER_ID;

@Feature("Get Player by ID")
public class PlayerGetTests extends BaseTest {

    @Test(description = "Get Player by ID", groups = "requiresPlayer")
    public void getPlayerById() {
        PlayerIdDTO playerIdToGet = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);
        PlayerRequestDTO expectedPlayer = Session.context().get(CREATED_PLAYER, PlayerRequestDTO.class);

        PlayerResponseDTO actualPlayer = playerService.getPlayerByPlayerId(playerIdToGet)
                .then()
                .statusCode(200)
                .extract()
                .as(PlayerResponseDTO.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualPlayer.getId(), playerIdToGet.getPlayerId(), "Player ID mismatch");
        assertPlayer(softAssert, actualPlayer, expectedPlayer);
        softAssert.assertAll();
    }

    @Issue("BUG-8")
    @Description("Response returns 200 and empty body if get deleted Player, but 404 is expected")
    @Test(description = "Get deleted Player by ID", groups = "requiresPlayer")
    public void getDeletedPlayerById() {
        PlayerIdDTO playerToGet = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);

        playerService.deletePlayer(playerToGet, Role.SUPERVISOR)
                .then().statusCode(204);

        playerService.getPlayerByPlayerId(playerToGet)
                .then().statusCode(404);
    }
}
