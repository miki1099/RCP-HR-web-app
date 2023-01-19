package pl.uginf.rcphrwebapp.hr.user.service;

import java.util.Date;
import java.util.List;

import pl.uginf.rcphrwebapp.hr.daysoff.dto.NewTimeOffRecord;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.TimeOffRecord;
import pl.uginf.rcphrwebapp.hr.user.dto.LoginRecord;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto getUserDtoByUsername(String username);

    User getUserByUsername(String username);

    List<WorkInfoDto> getWorkInfosForUser(String username);

    WorkInfoDto addWorkInfo(WorkInfoDto workInfoDto);

    void deactivateUser(String username);

    TimeOffRecord addDaysOffForUser(NewTimeOffRecord newTimeOff);

    List<TimeOffRecord> getDaysOffForUserBetween(String username, Date from, Date to);

    List<TimeOffRecord> getNotApprovedDaysOffForUser(String username);

    List<UserDto> getAllTeamMembers(String managerUsername);

    void approveDaysOff(List<Long> daysOffIdList);

    List<UserDto> getAllUsers();

    UserDto loginUser(LoginRecord loginRecord);

    List<UserDto> getAllActiveUsers();
}
