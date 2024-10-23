package com.reservoir.core.entity;

public enum RoleEnum {
    ADMIN("ADMIN"), // 超级管理员
    OPERATOR("OPERATOR"), // 操作员
    USER("USER"),   // 普通用户
    GUEST("GUEST");  // 游客


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