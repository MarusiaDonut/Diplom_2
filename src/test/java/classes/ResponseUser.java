package classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUser {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;
    private String message;

    public String getAccessToken() {
        if (accessToken != null) {
            accessToken = accessToken.replace("Bearer ", "");
        }
        return accessToken;
    }
}
