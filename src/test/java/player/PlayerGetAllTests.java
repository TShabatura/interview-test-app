package player;

import dto.player.response.PlayerGetAllResponseDTO;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Feature("Get all Players")
public class PlayerGetAllTests extends BaseTest {

    @Issue("BUG-7")
    @Description("Response doesn't have 'role' property")
    @Test(description = "Get all created players using getAll",
            groups = {"requiresPlayer", "requiresAdmin", "requiresUser"})
    public void getAllPlayers() {
        List<PlayerGetAllResponseDTO.PlayerItem> playersList = playerService.getAllPlayers()
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("jsonSchemas/get-all.json"))
                .extract()
                .as(PlayerGetAllResponseDTO.class).getPlayers();

        List<PlayerGetAllResponseDTO.PlayerItem> currentThreadItems = playersList.stream()
                .filter(playerItem -> playerItem.getScreenName().contains(Thread.currentThread().getName()))
                .toList();
        Assert.assertEquals(currentThreadItems.size(), 3);
    }
}
