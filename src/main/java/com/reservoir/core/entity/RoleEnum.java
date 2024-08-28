package com.reservoir.core.entity;

public enum RoleEnum {
    ADMIN("ADMIN"),
    USER("USER"),
    GUEST("GUEST");

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return role;
    }
}