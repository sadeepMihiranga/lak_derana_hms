package lk.lakderana.hms.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.lakderana.hms.dto.FunctionDTO;
import lk.lakderana.hms.dto.TokenRequestDTO;
import lk.lakderana.hms.dto.TokenResponseDTO;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.security.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import static lk.lakderana.hms.security.JwtTokenUtil.*;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private ObjectMapper mapper = new ObjectMapper();

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        TokenRequestDTO tokenRequestDTO = null;

        try {
            final String tokenRequestJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            log.info("Received authentication request {} ", StringUtils.normalizeSpace(tokenRequestJson));
            tokenRequestDTO = mapper.readValue(tokenRequestJson, TokenRequestDTO.class);
        } catch (Exception e) {
            throw new OperationException("Error while parsing the authentication request");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                tokenRequestDTO.getUsername(),
                tokenRequestDTO.getPassword());
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();

        final String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(ACCESS_TOKEN_EXPIRE_1_MIN)
                .withIssuer(request.getRequestURL().toString())
                .withClaim(ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim(FUNCTIONS, user.getPermittedFunctions().stream().map(FunctionDTO::getFunctionId).collect(Collectors.toList()))
                .withClaim(DISPLAY_NAME, user.getName())
                .withClaim(USER_ID, user.getId())
                .sign(Algorithm.HMAC256(SECRET_KEY.getBytes()));

        final String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(ACCESS_TOKEN_EXPIRE_1_YEAR)
                .withIssuer(request.getRequestURL().toString())
                .sign(Algorithm.HMAC256(SECRET_KEY.getBytes()));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().
                writeValue(
                        response.getOutputStream(),
                        SuccessResponseHandler.generateResponse(
                                new TokenResponseDTO(access_token, refresh_token, TOKEN_PREFIX_BEARER.trim()),
                                "Token generated successfully", true, HttpStatus.OK.value())
                );
    }
}
