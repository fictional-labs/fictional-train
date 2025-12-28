package com.w.backend.domain.user.entity;

public class User {

    private Long id;
    private String username;
    private String password;
    private String role;

    protected User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static User create(String username, String password) {
        return new User(username, password, "USER");
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

}
