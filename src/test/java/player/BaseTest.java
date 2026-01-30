package player;

import data.enums.Role;
import dto.PlayerIdDTO;
import dto.player.request.PlayerRequestDTO;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import service.PlayerService;
import session.Session;

import java.util.List;

import static data.player.PlayerTestData.*;
import static session.SessionKey.*;

public abstract class BaseTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BaseTest.class);
    protected final PlayerService playerService = new PlayerService();

    @BeforeMethod(onlyForGroups = "requiresPlayer")
    public void createPlayerForTest() {
        PlayerRequestDTO playerRequest = getValidPlayer();

        Response rs = playerService.createPlayer(playerRequest, Role.SUPERVISOR);

        if (rs.statusCode() == 200) {
            Long createdPlayerId = rs.jsonPath().getLong("id");
            Session.context().put(CREATED_PLAYER_ID, new PlayerIdDTO(createdPlayerId));
            Session.context().put(CREATED_PLAYER, playerRequest);
            log.info("Created Player for precondition with ID: {}", createdPlayerId);
        }
    }

    @BeforeMethod(onlyForGroups = "requiresAdmin")
    public void createAdminForTest() {
        PlayerRequestDTO playerRequest = getAdminPlayer();

        Response rs = playerService.createPlayer(playerRequest, Role.SUPERVISOR);

        if (rs.statusCode() == 200) {
            Long createdPlayerId = rs.jsonPath().getLong("id");
            Session.context().put(CREATED_ADMIN_ID, new PlayerIdDTO(createdPlayerId));
            log.info("Created Player with Admin role for precondition with ID: {}", createdPlayerId);
        }
    }

    @BeforeMethod(onlyForGroups = "requiresUser")
    public void createUserForTest() {
        PlayerRequestDTO playerRequest = getUserPlayer();

        Response rs = playerService.createPlayer(playerRequest, Role.SUPERVISOR);

        if (rs.statusCode() == 200) {
            Long createdPlayerId = rs.jsonPath().getLong("id");
            Session.context().put(CREATED_USER_ID, new PlayerIdDTO(createdPlayerId));
            Session.context().put(CREATED_USER, playerRequest);
            log.info("Created Player with User role for precondition with ID: {}", createdPlayerId);
        }
    }

    @AfterMethod()
    public void deleteCreatedUsers() {
        List<PlayerIdDTO> playersToDelete = Session.context().getList(PLAYERS_ID_TO_DELETE, PlayerIdDTO.class);
        if (!playersToDelete.isEmpty()) {
            for (PlayerIdDTO playerId : playersToDelete) {
                playerService.deletePlayer(playerId, Role.SUPERVISOR);
            }
            log.info("Deleted {} Players created during the test", playersToDelete.size());
        }
    }

    @AfterMethod
    public void tearDown() {
        Session.destroy();
    }
}
