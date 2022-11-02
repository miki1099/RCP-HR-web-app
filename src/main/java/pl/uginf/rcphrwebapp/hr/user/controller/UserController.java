package pl.uginf.rcphrwebapp.hr.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;

@RestController
@AllArgsConstructor
@RequestMapping("hr/user")
public class UserController {

    private final UserSLO userSLO;

    @GetMapping(value = "/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userSLO.getUserDtoByUsername(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/create-user")
    public UserDto create(@RequestBody UserDto user) {
        return userSLO.addUser(user);
    }
}
