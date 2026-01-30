package dto.player.response;

import java.util.List;

public class PlayerGetAllResponseDTO {
    List<PlayerItem> players;

    public List<PlayerItem> getPlayers() {
        return players;
    }

    public static class PlayerItem {
        private Integer age;
        private String gender;
        private Long id;
        private String role;
        private String screenName;

        public Integer getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }

        public Long getId() {
            return id;
        }

        public String getRole() {
            return role;
        }

        public String getScreenName() {
            return screenName;
        }
    }
}
