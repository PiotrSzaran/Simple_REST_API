package simple_rest_api.model;

public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"), ROLE_SUBSCRIBED("ROLE_SUBSCRIBED"), ROLE_UNSUBSCRIBED("ROLE_UNSUBSCRIBED");

    private String fullName;

    Role(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
