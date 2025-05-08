package com.example.trainup.model.user;

public enum BaseRoles {
    ADMIN("ADMIN"), ATHLETE("ATHLETE"), GYM_OWNER("GYM_OWNER"), TRAINER("TRAINER");

    private final String roleName;

    BaseRoles(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
