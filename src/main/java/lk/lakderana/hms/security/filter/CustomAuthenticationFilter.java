package lk.lakderana.hms.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.lakderana.hms.dto.FunctionDTO;
import lk.lakderana.hms.dto.TokenRequestDTO;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.security.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.HashMap;
import java.util.Map;
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
            tokenRequestDTO = mapper.readValue(tokenRequestJson, TokenRequestDTO.class);
            log.info("Received authentication request {} ", StringUtils.normalizeSpace(tokenRequestJson));
        } catch (IOException e) {
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
                .withExpiresAt(ACCESS_TOKEN_EXPIRE_1_YEAR)
                .withIssuer(request.getRequestURL().toString())
                .withClaim(ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim(FUNCTIONS, user.getPermittedFunctions().stream().map(FunctionDTO::getFunctionId).collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(SECRET_KEY.getBytes()));

        final String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(ACCESS_TOKEN_EXPIRE_1_YEAR)
                .withIssuer(request.getRequestURL().toString())
                .sign(Algorithm.HMAC256(SECRET_KEY.getBytes()));

        Map<String, String> tokens = new HashMap<>();

        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        tokens.put("token_type", TOKEN_PREFIX_BEARER.trim());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
