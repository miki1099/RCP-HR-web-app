package pl.uginf.rcphrwebapp.hr.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.TimeOffRecord;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserService;
import pl.uginf.rcphrwebapp.rcp.worklog.IdListRecord;

@RequestMapping("/hr/manager")
@CrossOrigin(origins = "http://localhost:8081")
@AllArgsConstructor
@RestController
public class ManagerController {

    UserService userService;

    @GetMapping(value = "/team-members")
    public List<UserDto> getAllTeamMembers(@RequestParam("manager") String managerUsername) {
        return userService.getAllTeamMembers(managerUsername);
    }

    @GetMapping(value = "/not-approved-days-off")
    public List<TimeOffRecord> getNotApprovedDaysOff(@RequestParam("username") String username) {
        return userService.getNotApprovedDaysOffForUser(username);
    }

    @PatchMapping(value = "/approve-days-off")
    public void approveDaysOff(@RequestBody IdListRecord daysOffIdList) {
        userService.approveDaysOff(daysOffIdList.idList());
    }
}