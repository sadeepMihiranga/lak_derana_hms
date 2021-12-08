package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.Role;
import lk.lakderana.hms.entity.RoleToUser;
import lk.lakderana.hms.entity.User;
import lk.lakderana.hms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getAUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(userService.getAUserById(userId));
    }

    @PostMapping
    public ResponseEntity<User> createAUser(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    @PostMapping("/role")
    public ResponseEntity<Role> createARole(@RequestBody Role role) {
        return ResponseEntity.ok().body(userService.createRole(role));
    }

    @PostMapping("/role/assign")
    public ResponseEntity<?> assignRoleToUser(@RequestBody RoleToUser roleToUser) {
        userService.addRoleToUser(roleToUser.getUser().getUsername(), roleToUser.getRole().getName());
        return ResponseEntity.ok().build();
    }
}
