package gamo.villalba.sergio.dtos;

public class SignInDto {

    private String username;
    private String password;

    public SignInDto() {}

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
}
