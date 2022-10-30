package pl.uginf.rcphrwebapp.hr.user.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uginf.rcphrwebapp.exceptions.UserNotFoundException;
import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.hr.user.User;
import pl.uginf.rcphrwebapp.hr.user.UserRepository;
import pl.uginf.rcphrwebapp.hr.user.UserValidator;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;

import java.text.ParseException;
import java.util.Optional;

import static pl.uginf.rcphrwebapp.utils.MsgCodes.NOT_UNIQUE;

@AllArgsConstructor(onConstructor_ = @Autowired)
@Service
public class UserSLOBean implements UserSLO {

    private final UserValidator userValidator;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public UserDto addUser(UserDto userDto) throws ParseException {
        userValidator.validate(userDto);
        checkIfUserExists(userDto);
        User userToSave = modelMapper.map(userDto, User.class);
        userRepository.save(userToSave);
        return userDto;
    }

    @Override
    public UserDto getUserByUsername(String username) {
        Optional<User> userDB = userRepository.getUserByUsername(username);
        if (userDB.isPresent()) {
            return modelMapper.map(userDB.get(), UserDto.class);
        } else
            throw new UserNotFoundException(username);
    }

    private void checkIfUserExists(UserDto userDto) {
        if (userRepository.existsByPesel(userDto.getPesel())) {
            throw new ValidationException(NOT_UNIQUE.getMsg("Pesel"));
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ValidationException(NOT_UNIQUE.getMsg("Email"));
        }
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ValidationException(NOT_UNIQUE.getMsg("Username"));
        }
    }
}
