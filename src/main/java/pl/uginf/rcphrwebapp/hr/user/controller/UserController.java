package pl.uginf.rcphrwebapp.hr.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;

import java.text.ParseException;

@RestController
@AllArgsConstructor
@CrossOrigin("https://localhost:8082")
public class UserController {

    private final UserSLO userSLO;

    @GetMapping(value = "/user/{username}")
    public UserDto getUserByUsername(@PathVariable String username) throws Exception {
        return userSLO.getUserByUsername(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/hr/create-user")
    public UserDto create(@RequestBody UserDto user) throws ParseException {
        return userSLO.addUser(user);
    }
}
