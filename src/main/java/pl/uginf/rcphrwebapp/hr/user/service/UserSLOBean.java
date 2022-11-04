package pl.uginf.rcphrwebapp.hr.user.service;

import static pl.uginf.rcphrwebapp.utils.MsgCodes.NOT_UNIQUE;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.exceptions.UserNotFoundException;
import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.hr.user.UserRepository;
import pl.uginf.rcphrwebapp.hr.user.UserValidator;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfo;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoRepository;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoValidator;

@AllArgsConstructor(onConstructor_ = @Autowired)
@Service
public class UserSLOBean implements UserSLO {

    private final UserValidator userValidator;

    private final WorkInfoValidator workInfoValidator;

    private final UserRepository userRepository;

    private final WorkInfoRepository workInfoRepository;

    private final ModelMapper modelMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        userValidator.validate(userDto);
        checkIfUserExists(userDto);
        User userToSave = modelMapper.map(userDto, User.class);
        userRepository.save(userToSave);
        return userDto;
    }

    @Override
    public UserDto getUserDtoByUsername(String username) {
        User user = getByUsername(username);
        return modelMapper.map(user, UserDto.class);
    }

    public User getUserByUsername(String username) {
        return getByUsername(username);
    }

    @Override
    public List<WorkInfoDto> getWorkInfosForUser(String username) {
        checkUserExistence(username);
        List<WorkInfo> allByUserId_username = workInfoRepository.findAllByUserId_Username(username);

        return allByUserId_username.stream()
                .map(workInfo -> {
                    WorkInfoDto workInfoDto = modelMapper.map(workInfo, WorkInfoDto.class);
                    workInfoDto.setUsername(workInfo.getUserId()
                            .getUsername());
                    return workInfoDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkInfoDto addWorkInfo(WorkInfoDto workInfoDto) {
        workInfoValidator.validate(workInfoDto);
        WorkInfo workInfo = modelMapper.map(workInfoDto, WorkInfo.class);
        User user = getByUsername(workInfoDto.getUsername());
        workInfo.setUserId(user);
        workInfoRepository.save(workInfo);
        return workInfoDto;
    }

    @Override
    @Transactional
    public void deactivateUser(String username) {
        User userToDeactivate = getByUsername(username);
        userToDeactivate.setActive(false);
        List<WorkInfo> workInfos = userToDeactivate.getWorkInfos();
        for (WorkInfo workInfo : workInfos) {
            if ( workInfo.getTo() == null ) {
                workInfo.setTo(new Date());
                break;
            }
        }
        userRepository.save(userToDeactivate);
    }

    private User getByUsername(String username) {
        Optional<User> userDB = userRepository.findUserByUsername(username);
        return userDB.orElseThrow(() -> new UserNotFoundException(username));
    }

    private void checkUserExistence(String username) {
        if ( !userRepository.existsByUsername(username) ) {
            throw new UserNotFoundException(username);
        }
    }

    private void checkIfUserExists(UserDto userDto) {
        if ( userRepository.existsByPesel(userDto.getPesel()) ) {
            throw new ValidationException(NOT_UNIQUE.getMsg("Pesel"));
        }
        if ( userRepository.existsByEmail(userDto.getEmail()) ) {
            throw new ValidationException(NOT_UNIQUE.getMsg("Email"));
        }
        if ( userRepository.existsByUsername(userDto.getUsername()) ) {
            throw new ValidationException(NOT_UNIQUE.getMsg("Username"));
        }
    }
}