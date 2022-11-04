package pl.uginf.rcphrwebapp.hr.hr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;

@RestController
@AllArgsConstructor
@RequestMapping("/hr/user")
public class HrController {

    UserSLO userSLO;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/deactivate-user")
    public void deactivateUser(@RequestParam("user") String username) {
        userSLO.deactivateUser(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/create-user")
    public UserDto createUser(@RequestBody UserDto user) {
        return userSLO.addUser(user);
    }

    @PostMapping(value = "/add-work-info")
    public WorkInfoDto addWorkInfo(@RequestBody WorkInfoDto workInfoDto) {
        return userSLO.addWorkInfo(workInfoDto);
    }

}
