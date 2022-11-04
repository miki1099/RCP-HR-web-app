package pl.uginf.rcphrwebapp.hr.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserSLO userSLO;

    @GetMapping(value = "/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userSLO.getUserDtoByUsername(username);
    }
}
