package pl.uginf.rcphrwebapp.hr.workinfo.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;

import java.util.List;

@RestController
@RequestMapping("hr/workInfo")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class WorkInfoController {

    private final UserSLO userSLO;

    @GetMapping(value = "/getAllForUser/{username}")
    public List<WorkInfoDto> getWorkInfosForUser(@PathVariable String username) {
        return userSLO.getWorkInfosForUser(username);
    }

    @PostMapping(value = "/add")
    public WorkInfoDto addWorkInfo(@RequestBody WorkInfoDto workInfoDto) {
        return userSLO.addWorkInfo(workInfoDto);
    }
}
