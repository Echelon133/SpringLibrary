package ml.echelon133.model.dto;

import ml.echelon133.model.validator.PasswordsMatch;
import org.hibernate.validator.constraints.Length;


@PasswordsMatch
public class NewUserDto {

    @Length(min = 4, max = 50)
    private String username;

    @Length(min = 6, max = 100)
    private String password;

    private String password2;

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

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
