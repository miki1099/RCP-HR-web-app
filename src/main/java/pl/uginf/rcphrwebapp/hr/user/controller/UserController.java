package pl.uginf.rcphrwebapp.hr.user.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.NewTimeOffRecord;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.TimeOffRecord;
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

    @PutMapping(value = "/addDaysOff")
    public TimeOffRecord addTimeOff(@RequestBody NewTimeOffRecord newTimeOff) {
        return userSLO.addDaysOffForUser(newTimeOff);
    }

    @GetMapping(value = "/{username}/getAllDaysOffBetween")
    public List<TimeOffRecord> getDaysOffAfter(@PathVariable String username, @RequestParam("fromDate") Date from, @RequestParam("toDate") Date to) {
        return userSLO.getDaysOffForUserBetween(username, from, to);
    }

    @GetMapping(value = "/{username}/getNotApprovedFrom")
    public List<TimeOffRecord> getNotApprovedDaysOffAfter(@PathVariable String username, @RequestParam("fromDate") Date from) {
        return userSLO.getNotApprovedDaysOffForUserBetween(username, from);
    }

}
