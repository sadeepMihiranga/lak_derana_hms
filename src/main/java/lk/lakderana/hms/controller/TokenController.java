package lk.lakderana.hms.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.lakderana.hms.dto.FunctionDTO;
import lk.lakderana.hms.dto.TokenResponseDTO;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.security.User;
import lk.lakderana.hms.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import static lk.lakderana.hms.security.JwtTokenUtil.*;

@CrossOrigin
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

                final User user = userService.getUserDetailsByUsername(username);

                final String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                        .withExpiresAt(ACCESS_TOKEN_EXPIRE_1_MIN)
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim(ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .withClaim(FUNCTIONS, user.getPermittedFunctions().stream().map(FunctionDTO::getFunctionId).collect(Collectors.toList()))
                        .withClaim(DISPLAY_NAME, user.getName())
                        .withClaim(PARTY_CODE, user.getPartyCode())
                        .withClaim(BRANCH_CODE, user.getBranchCode())
                        .withClaim(USER_ID, user.getId())
                        .sign(Algorithm.HMAC256(SECRET_KEY.getBytes()));

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(
                                response.getOutputStream(),
                                SuccessResponseHandler.generateResponse(
                                        new TokenResponseDTO(access_token, refresh_token, TOKEN_PREFIX_BEARER.trim()),
                                        "Token generated successfully", true, HttpStatus.OK.value())
                        );

            } catch (Exception e) {
                response.setHeader("Error", e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        SuccessResponseHandler.generateResponse(null, e.getMessage(), true, HttpServletResponse.SC_FORBIDDEN)
                );
            }
        } else {
            throw new RuntimeException("Refresh Token is missing.");
        }
    }
}
