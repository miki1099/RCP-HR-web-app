package pl.uginf.rcphrwebapp.hr.user.service;

import java.sql.Date;
import java.util.List;

import pl.uginf.rcphrwebapp.hr.daysoff.dto.NewTimeOffRecord;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.TimeOffRecord;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;

public interface UserSLO {

    UserDto addUser(UserDto userDto);

    UserDto getUserDtoByUsername(String username);

    User getUserByUsername(String username);

    List<WorkInfoDto> getWorkInfosForUser(String username);

    WorkInfoDto addWorkInfo(WorkInfoDto workInfoDto);

    void deactivateUser(String username);

    TimeOffRecord addDaysOffForUser(NewTimeOffRecord newTimeOff);

    List<TimeOffRecord> getDaysOffForUserBetween(String username, Date from, Date to);

    List<TimeOffRecord> getNotApprovedDaysOffForUserBetween(String username, Date from);
}
