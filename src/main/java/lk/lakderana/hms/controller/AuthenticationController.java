package lk.lakderana.hms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.lakderana.hms.dto.AuthenticationRequestDTO;
import lk.lakderana.hms.dto.JwtAuthenticationResponse;
import lk.lakderana.hms.dto.ResetPasswordDTO;
import lk.lakderana.hms.dto.UpdatePasswordDTO;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.security.AuthenticationGateway;
import lk.lakderana.hms.security.config.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping({"/api/auth"})
public class AuthenticationController {

    private final AuthenticationGateway authenticationGateway;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AuthenticationGateway authenticationGateway,
                                    AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider) {
        this.authenticationGateway = authenticationGateway;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "/token", consumes = {"application/json", "application/x-www-form-urlencoded"})
    public ResponseEntity<JwtAuthenticationResponse> getToken(@RequestBody String payload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AuthenticationRequestDTO loginRequest = mapper.readValue(payload, AuthenticationRequestDTO.class);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, refreshToken));
    }

    @PostMapping(value = "/token/refresh")
    public ResponseEntity<JwtAuthenticationResponse> getTokenUsingRefreshToken(@RequestBody String payload) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(payload);
            String refreshToken = jsonNode.get("refresh_token").textValue();
            Map<String, String> tokenMap = jwtTokenProvider.generateTokenPairFromRefreshToken(refreshToken);
            return ResponseEntity.ok(new JwtAuthenticationResponse(tokenMap.get("jwt"), tokenMap.get("refresh")));
        } catch (JsonProcessingException e) {
            throw new OperationException("Failed to parse the JSON payload");
        }
    }

    @PostMapping(value = "/createPassword")
    public ResponseEntity<SuccessResponse> generatePasswordCreateToken(@RequestBody String resetPasswordJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResetPasswordDTO resetPassword = mapper.readValue(resetPasswordJson, ResetPasswordDTO.class);
        SuccessResponse response = authenticationGateway
                .generateResetPasswordToken(resetPassword, "password_reset_link_email_with_username.html", "Create Account");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/resetPassword")
    public ResponseEntity<SuccessResponse> generatePasswordResetToken(@RequestBody String resetPasswordJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResetPasswordDTO resetPassword = mapper.readValue(resetPasswordJson, ResetPasswordDTO.class);
        SuccessResponse response = authenticationGateway
                .generateResetPasswordToken(resetPassword, "password_reset_link_email.html", "Reset Password");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // if type = CEMIL -> username = party code, else it's username
    @PutMapping(value = "/userPassword")
    public ResponseEntity<SuccessResponse> updatePassword(@RequestBody String updatePasswordJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UpdatePasswordDTO updatePassword = mapper.readValue(updatePasswordJson, UpdatePasswordDTO.class);
        SuccessResponse response = authenticationGateway.updUserPassword(updatePassword);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/verifyPin")
    public ResponseEntity<SuccessResponse> verifyPIN(@RequestParam("username") String username,
                                                     @RequestParam("pinNo") String pin) {
        SuccessResponse response = authenticationGateway.verifyResetPasswordForPinNo(username, pin);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/verifyEmailLink")
    public ResponseEntity<SuccessResponse> verifyEmailLink(@RequestParam("partyCode") String partyCode,
                                                           @RequestParam("token") String token) {
        SuccessResponse response = authenticationGateway.verifyResetPasswordForTokenString(partyCode, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
