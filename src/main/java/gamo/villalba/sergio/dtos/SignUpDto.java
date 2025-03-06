package gamo.villalba.sergio.dtos;

import gamo.villalba.sergio.enums.UserRole;

public class SignUpDto {
    private String username;
    private String password;
    private UserRole role;

    public SignUpDto() {}

    public SignUpDto(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}