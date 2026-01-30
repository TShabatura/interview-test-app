package data.enums;

public enum Role {

    ADMIN("admin"),
    SUPERVISOR("supervisor"),
    USER("user");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
