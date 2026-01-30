package player;

import data.enums.Role;
import data.player.PlayerTestData;
import dto.PlayerIdDTO;
import dto.player.request.PlayerRequestDTO;
import dto.player.response.PlayerResponseDTO;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import session.Session;

import static assertion.PlayerAssertion.*;
import static data.player.PlayerTestData.getPlayerRequestWithChangedField;
import static data.player.PlayerTestData.getValidPlayer;
import static session.SessionKey.CREATED_PLAYER_ID;

public class PlayerUpdateTests extends BaseTest {

    @Issue("CI-1")
    @Description("Clarification issue: response doesn't return password property. Should be discussed if it's expected")
    @Test(
            description = "Update all player properties with valid data — expect 200 and populated DTO",
            groups = {"requiresPlayer", "requiresAdmin"},
            dataProvider = "adminRoles", dataProviderClass = PlayerTestData.class
    )
    public void updatePlayerWithValidData(Role role) {
        Allure.parameter("Update using Role", role);

        Long createdPlayerId = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class).getPlayerId();
        PlayerRequestDTO playerUpdateRq = getValidPlayer();

        PlayerResponseDTO updatedPlayer = playerService.updatePlayer(createdPlayerId, playerUpdateRq, role)
                .then()
                .statusCode(200)
                .extract()
                .as(PlayerResponseDTO.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(updatedPlayer.getId(), createdPlayerId, "Player ID should remain the same after update");
        assertPlayer(softAssert, updatedPlayer, playerUpdateRq);
        softAssert.assertAll();
    }

    @Test(
            description = "Update single player field — expect 200 and only that field changed",
            groups = "requiresPlayer",
            dataProvider = "fieldsForUpdate", dataProviderClass = PlayerTestData.class
    )
    public void updatePlayersWithOneField(String fieldToUpdate) {
        Allure.parameter("Field to update", fieldToUpdate);

        PlayerIdDTO createdPlayerIdDTO = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);
        Long createdPlayerId = createdPlayerIdDTO.getPlayerId();
        PlayerResponseDTO createdPlayer = playerService.getPlayerByPlayerId(createdPlayerIdDTO)
                .then()
                .extract()
                .as(PlayerResponseDTO.class);

        PlayerRequestDTO playerUpdateRq = getPlayerRequestWithChangedField(fieldToUpdate);

        PlayerResponseDTO updatedPlayer = playerService.updatePlayer(createdPlayerId, playerUpdateRq, Role.SUPERVISOR)
                .then()
                .statusCode(200)
                .extract()
                .as(PlayerResponseDTO.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(updatedPlayer.getId(), createdPlayerId, "Player ID should remain the same after update");
        assertEqualField(softAssert, updatedPlayer, playerUpdateRq, fieldToUpdate);
        assertEqualExcept(softAssert, updatedPlayer, createdPlayer, fieldToUpdate);
        softAssert.assertAll();
    }

    @Issue("BUG-9")
    @Description("Update response returns 200 after update Player that doesn't exist, but 404 is expected")
    @Test(description = "Update non-existing player — expect 404", groups = "requiresPlayer")
    public void updateNonExistingPlayer() {
        PlayerIdDTO createdPlayerIdDTO = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);

        playerService.deletePlayer(createdPlayerIdDTO, Role.SUPERVISOR);

        Long deletedPlayerId = createdPlayerIdDTO.getPlayerId();
        PlayerRequestDTO playerUpdateRq = getValidPlayer();

        playerService.updatePlayer(deletedPlayerId, playerUpdateRq, Role.SUPERVISOR)
                .then()
                .statusCode(404);
    }

    @Issue("BUG-10")
    @Description("Player can be updated with invalid data")
    @Test(
            description = "Update player with invalid data — expect 400",
            groups = "requiresPlayer",
            dataProvider = "invalidDataForUpdate", dataProviderClass = PlayerTestData.class
    )
    public void updatePlayerWithInvalidData(String caseName, PlayerRequestDTO invalidRequest) {
        Allure.parameter("Test case", caseName);

        PlayerIdDTO createdPlayerIdDTO = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);
        Long createdPlayerId = createdPlayerIdDTO.getPlayerId();

        playerService.updatePlayer(createdPlayerId, invalidRequest, Role.SUPERVISOR)
                .then()
                .statusCode(400);
    }

    @Issue("BUG-11")
    @Description("User is able to update another Player, but it's forbidden operation for User role")
    @Test(
            description = "Update player as another user — expect 403 (forbidden)",
            groups = {"requiresUser", "requiresPlayer"}
    )
    public void updatePlayerAsUser() {
        PlayerIdDTO createdPlayerIdDTO = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);
        Long createdPlayerId = createdPlayerIdDTO.getPlayerId();
        PlayerRequestDTO playerUpdateRq = getValidPlayer();

        playerService.updatePlayer(createdPlayerId, playerUpdateRq, Role.USER)
                .then()
                .statusCode(403);
    }
}