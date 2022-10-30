package pl.uginf.rcphrwebapp.hr.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;

@RestController
@AllArgsConstructor
@RequestMapping("hr/user")
public class UserController {

    private final UserSLO userSLO;

    @GetMapping(value = "/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userSLO.getUserByUsername(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/create-user")
    public UserDto create(@RequestBody UserDto user) {
        return userSLO.addUser(user);
    }
}
