package lk.lak_derana_hms.controller;

import lk.lak_derana_hms.dto.UserDTO;
import lk.lak_derana_hms.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers()
    {
        return userService.getAllUsers();
    }
}
