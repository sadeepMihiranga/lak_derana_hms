package lk.lak_derana_hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.lak_derana_hms.dto.AuthenticationRequestDTO;
import lk.lak_derana_hms.dto.JwtAuthenticationResponseDTO;
import lk.lak_derana_hms.security.ApplicationUser;
import lk.lak_derana_hms.security.jwt.JwtTokenProvider;
import lk.lak_derana_hms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping({"/auth"})
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider,
                                    UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping(value = "/token", consumes = {"application/json", "application/x-www-form-urlencoded"})
    public ResponseEntity<JwtAuthenticationResponseDTO> getToken(@RequestBody String payload) throws IOException {

        AuthenticationRequestDTO loginRequest = new ObjectMapper().readValue(payload, AuthenticationRequestDTO.class);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        //String jwt = jwtTokenProvider.generateToken(authentication);

        final ApplicationUser applicationUser = userService.loadUserUserByUsername(loginRequest.getUsername());

        String jwt = jwtTokenProvider.generateToken(applicationUser);
        /*String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        Optional<UserSessionDTO> sessionOpt = userSessionService.createSession(loginRequest.getUsername(), refreshToken);
        UserSessionDTO session = null;
        if(sessionOpt.isPresent())
            session = sessionOpt.get();
        else
            throw new InvalidDataException("Invalid Session");

        return ResponseEntity.ok(new JwtAuthenticationResponseDTO(jwt, session.getSessionId()));*/
        return ResponseEntity.ok(new JwtAuthenticationResponseDTO(jwt, String.valueOf(System.currentTimeMillis())));
    }
}
