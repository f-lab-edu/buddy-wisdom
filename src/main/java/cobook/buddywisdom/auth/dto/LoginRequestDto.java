package cobook.buddywisdom.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.apache.logging.log4j.util.Base64Util.encode;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequestDto {
    @Email
    private String email;
    private String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = encode(password);
    }
}
