package pl.uginf.rcphrwebapp.hr.manager.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.TimeOffRecord;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserService;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

@RequestMapping("/hr/manager")
@AllArgsConstructor
@RestController
public class ManagerController { //TODO description of front logic: get all team members, choose, get user work log and not approved days off

    UserService userService;

    @GetMapping(value = "/team-members")
    public List<UserDto> getAllTeamMembers(String managerUsername) {
        return userService.getAllTeamMembers(managerUsername);
    } //TODO only user or user with work info

    @GetMapping(value = "/user-worklog")
    public List<WorkLog> getUserWorkLogBetween(@RequestParam("user") String username, @RequestParam("from") Date from, @RequestParam("to") Date to) {
        return userService.getWorkLogsForUser(username, from, to);
    }

    @GetMapping(value = "/not-approved-days-off")
    public List<TimeOffRecord> getNotApprovedDaysOff(@RequestParam("user") String username) {
        return userService.getNotApprovedDaysOffForUser(username);
    }

    @PostMapping(value = "/approve-days-off")
    public void approveDaysOff(@RequestParam("daysOffId") List<Long> daysOffIdList) {
        userService.approveDaysOff(daysOffIdList);
    } //TODO days off or times off
    //TODO if this should return not approved again?
}