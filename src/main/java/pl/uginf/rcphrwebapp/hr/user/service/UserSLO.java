package pl.uginf.rcphrwebapp.hr.user.service;

import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;

import java.text.ParseException;

public interface UserSLO {

    UserDto addUser(UserDto userDto) throws ParseException;

    UserDto getUserByUsername(String username) throws Exception;
}
