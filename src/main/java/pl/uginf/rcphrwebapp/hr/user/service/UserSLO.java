package pl.uginf.rcphrwebapp.hr.user.service;

import java.util.List;

import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;

public interface UserSLO {

    UserDto addUser(UserDto userDto);

    UserDto getUserDtoByUsername(String username);

    User getUserByUsername(String username);

    List<WorkInfoDto> getWorkInfosForUser(String username);

    WorkInfoDto addWorkInfo(WorkInfoDto workInfoDto);
}
