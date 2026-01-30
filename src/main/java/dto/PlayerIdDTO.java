package dto;

public class PlayerIdDTO {

    private final Long playerId;

    public Long getPlayerId() {
        return playerId;
    }

    public PlayerIdDTO (Long playerId) {
        this.playerId = playerId;
    }
}