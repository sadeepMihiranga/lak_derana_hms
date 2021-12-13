package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.TMsRole;
import lk.lakderana.hms.entity.TMsUserRole;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<SuccessResponse> getAllUsers() {
        return SuccessResponseHandler.generateResponse(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<SuccessResponse> getAUser(@PathVariable("userId") Long userId) {
        return SuccessResponseHandler.generateResponse(userService.getAUserById(userId));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createAUser(@RequestBody UserDTO userDTO) {
        return SuccessResponseHandler.generateResponse(userService.createUser(userDTO));
    }

    @PostMapping("/role")
    public ResponseEntity<TMsRole> createARole(@RequestBody TMsRole role) {
        return ResponseEntity.ok().body(userService.createRole(role));
    }

    @PostMapping("/role/assign")
    public ResponseEntity<?> assignRoleToUser(@RequestBody TMsUserRole roleToUser) {
        userService.addRoleToUser(roleToUser.getUser().getUserUsername(), roleToUser.getRole().getRoleName());
        return ResponseEntity.ok().build();
    }
}
