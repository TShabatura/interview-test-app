package player;

import data.enums.Role;
import dto.PlayerIdDTO;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import session.Session;

import static assertion.PlayerAssertion.assertDeleteNonExisting;
import static assertion.PlayerAssertion.assertDeleteSuccess;
import static session.SessionKey.CREATED_PLAYER_ID;

@Feature("Delete Player")
public class PlayerDeleteTests extends BaseTest {

    @Test(description = "Delete player by ID — expect 204", groups = "requiresPlayer")
    public void deletePlayerById() {
        PlayerIdDTO playerToDelete = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);

        playerService.deletePlayer(playerToDelete, Role.SUPERVISOR)
                .then()
                .statusCode(204);
    }

    @Test(description = "Delete player as Admin — expect 204", groups = {"requiresPlayer", "requiresAdmin"})
    public void deletePlayerAsAdmin() {
        PlayerIdDTO playerToDelete = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);

        playerService.deletePlayer(playerToDelete, Role.ADMIN)
                .then()
                .statusCode(204);
    }

    @Issue("BUG-4")
    @Description("Response returns 403, but 401 is expected, as Admin player doesn't exist and cannot be authorized")
    @Test(description = "Delete player as non-existing Admin — expect 401 (unauthorized)", groups = "requiresPlayer")
    public void deletePlayerAsNonExistingAdmin() {
        PlayerIdDTO playerToDelete = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);

        playerService.deletePlayer(playerToDelete, Role.ADMIN)
                .then()
                .statusCode(401);
    }

    @Issue("BUG-5")
    @Description("Delete response returns 403 after delete Player that doesn't exist, but 204/404 is expected")
    @Test(description = "Delete same player twice — expect 204 then handled as non-existing", groups = "requiresPlayer")
    public void deletePlayerThatDoesNotExist() {
        PlayerIdDTO playerToDelete = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);
        Response response;

        response = playerService.deletePlayer(playerToDelete, Role.SUPERVISOR);
        assertDeleteSuccess(response);

        response = playerService.deletePlayer(playerToDelete, Role.SUPERVISOR);
        assertDeleteNonExisting(response);
    }

    @Issue("BUG-6")
    @Description("User is able to delete, but it's forbidden operation for User role")
    @Test(
            description = "Delete player by ID as User — expect 403 (forbidden)",
            groups = {"requiresPlayer", "requiresUser"}
    )
    public void deletePlayerByIdAsUser() {
        PlayerIdDTO playerToDelete = Session.context().get(CREATED_PLAYER_ID, PlayerIdDTO.class);

        playerService.deletePlayer(playerToDelete, Role.USER)
                .then()
                .statusCode(403);
    }
}
