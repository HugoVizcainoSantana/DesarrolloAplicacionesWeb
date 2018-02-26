package daw.spring.model;

public enum Roles {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");
    private String roleName;

    Roles(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
