package pl.uginf.rcphrwebapp.hr.workinfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.user.service.UserService;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;

@RestController
@RequestMapping("/workInfo")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class WorkInfoController {

    private final UserService userService;

    @GetMapping(value = "/getAllForUser/{username}")
    public List<WorkInfoDto> getWorkInfosForUser(@PathVariable String username) {
        return userService.getWorkInfosForUser(username);
    }
}
