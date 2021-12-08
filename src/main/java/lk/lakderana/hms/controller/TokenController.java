package lk.lakderana.hms.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static lk.lakderana.hms.security.JwtTokenUtil.*;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final UserService userService;

    public TokenController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/refresh")
    public void createRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX_BEARER)) {
            try {
                final String refresh_token = authorizationHeader.substring(TOKEN_PREFIX_BEARER.length());
                final DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY.getBytes())).build().verify(refresh_token);
                final String username = decodedJWT.getSubject();

                UserDTO user = userService.getAUser(username);

                final String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(ACCESS_TOKEN_EXPIRE_10_MIN)
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim(ROLES, user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()))
                        .sign(Algorithm.HMAC256(SECRET_KEY.getBytes()));

                Map<String, String> tokens = new HashMap<>();

                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                tokens.put("token_type", TOKEN_PREFIX_BEARER.trim());

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e) {
                response.setHeader("Error", e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh Token is missing.");
        }
    }
}
