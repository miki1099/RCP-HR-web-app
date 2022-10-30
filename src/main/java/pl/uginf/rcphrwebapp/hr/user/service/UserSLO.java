package pl.uginf.rcphrwebapp.hr.user.service;

import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;

import java.util.List;

public interface UserSLO {

    UserDto addUser(UserDto userDto);

    UserDto getUserByUsername(String username);

    List<WorkInfoDto> getWorkInfosForUser(String username);

    WorkInfoDto addWorkInfo(WorkInfoDto workInfoDto);
}
