package lk.lakderana.hms.dto;

import lk.lakderana.hms.security.JwtTokenUtil;
import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = JwtTokenUtil.TOKEN_PREFIX_BEARER;

    public JwtAuthenticationResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
