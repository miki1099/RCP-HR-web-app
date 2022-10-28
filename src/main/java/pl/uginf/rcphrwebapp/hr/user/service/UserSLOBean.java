package pl.uginf.rcphrwebapp.hr.user.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uginf.rcphrwebapp.hr.ValidationException;
import pl.uginf.rcphrwebapp.hr.user.User;
import pl.uginf.rcphrwebapp.hr.user.UserRepository;
import pl.uginf.rcphrwebapp.hr.user.UserValidator;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;

import static pl.uginf.rcphrwebapp.utils.MsgCodes.NOT_UNIQUE;

@AllArgsConstructor(onConstructor_ = @Autowired)
@Service
public class UserSLOBean implements UserSLO {

    private final UserValidator userValidator;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public void addUser(UserDto userDto) {
        userValidator.validate(userDto);
        checkIfUserExists(userDto);
        User userToSave = modelMapper.map(userDto, User.class);
        userRepository.save(userToSave);
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
