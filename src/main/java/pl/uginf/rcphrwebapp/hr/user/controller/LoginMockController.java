package pl.uginf.rcphrwebapp.hr.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.uginf.rcphrwebapp.hr.user.dto.LoginRecord;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserService;

@RestController
@RequestMapping("/login")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class LoginMockController {

    private final UserService userService;

    @PostMapping
    public UserDto login(@RequestBody LoginRecord loginRecord) {
        return userService.loginUser(loginRecord);
    }
}
