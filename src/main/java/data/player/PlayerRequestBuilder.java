package data.player;

import data.enums.Role;
import dto.player.request.PlayerRequestDTO;

import static util.RandomDataGenerator.*;

public class PlayerRequestBuilder {

    private Integer age = 25;
    private String gender = getRandomGender();
    private String login = getRandomLogin();
    private String password = getRandomPassword(10);
    private String role = Role.USER.value();
    private String screenName = getRandomScreenName();

    public PlayerRequestBuilder withAge(Integer age) {
        this.age = age;
        return this;
    }

    public PlayerRequestBuilder withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public PlayerRequestBuilder withLogin(String login) {
        this.login = login;
        return this;
    }

    public PlayerRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public PlayerRequestBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    public PlayerRequestBuilder withScreenName(String screenName) {
        this.screenName = screenName;
        return this;
    }

    public PlayerRequestBuilder withoutAge() {
        this.age = null;
        return this;
    }

    public PlayerRequestBuilder withoutGender() {
        this.gender = null;
        return this;
    }

    public PlayerRequestBuilder withoutLogin() {
        this.login = null;
        return this;
    }

    public PlayerRequestBuilder withoutPassword() {
        this.password = null;
        return this;
    }

    public PlayerRequestBuilder withoutRole() {
        this.role = null;
        return this;
    }

    public PlayerRequestBuilder withoutScreenName() {
        this.screenName = null;
        return this;
    }

    public PlayerRequestDTO build() {
        PlayerRequestDTO playerRequest = new PlayerRequestDTO();
        playerRequest.setAge(age);
        playerRequest.setGender(gender);
        playerRequest.setLogin(login);
        playerRequest.setPassword(password);
        playerRequest.setRole(role);
        playerRequest.setScreenName(screenName);
        return playerRequest;
    }
}
