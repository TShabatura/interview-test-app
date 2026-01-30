package service;

import data.enums.Role;
import dto.PlayerIdDTO;
import dto.player.request.PlayerRequestDTO;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.Session;

import static io.restassured.RestAssured.given;
import static session.SessionKey.PLAYERS_ID_TO_DELETE;
import static util.Mapper.convertToQueryParams;

public class PlayerService extends BaseService {

    private static final Logger log = LoggerFactory.getLogger(PlayerService.class);

    public Response createPlayer(PlayerRequestDTO playerData, Role editor) {
        Response response = given()
                .spec(baseSpec())
                .pathParam("editor", editor.value())
                .queryParams(convertToQueryParams(playerData))
                .when()
                .get("player/create/{editor}");

        if (response.statusCode() == HttpStatus.SC_OK) {
            Long createdPlayerId = response.jsonPath().getLong("id");
            log.info("Player created with ID: {}", createdPlayerId);
            Session.context().addToList(PLAYERS_ID_TO_DELETE, new PlayerIdDTO(createdPlayerId));
        }

        return response;
    }

    public Response deletePlayer(PlayerIdDTO request, Role editor) {
        Response response = given()
                .spec(baseSpec())
                .pathParam("editor", editor.value())
                .body(request)
                .when()
                .delete("player/delete/{editor}");

        log.info("Delete Player with id: {} response status code: {}", request.getPlayerId(), response.statusCode());

        return response;
    }

    public Response getPlayerByPlayerId(PlayerIdDTO request) {
        Response response = given()
                .spec(baseSpec())
                .body(request)
                .when()
                .post("player/get");

        log.info("Get Player with id: {} response status code: {}", request.getPlayerId(), response.statusCode());

        return response;
    }

    public Response getAllPlayers() {
        Response response = given()
                .spec(baseSpec())
                .when()
                .get("player/get/all");

        log.info("Get all Players response status code: {}", response.statusCode());

        return response;
    }

    public Response updatePlayer(Long id, PlayerRequestDTO body, Role editor) {
        Response response = given()
                .spec(baseSpec())
                .pathParam("id", id)
                .pathParam("editor", editor.value())
                .body(body)
                .when()
                .patch("player/update/{editor}/{id}");

        log.info("Update Player with id: {} response status code: {}", id, response.statusCode());

        return response;
    }
}
