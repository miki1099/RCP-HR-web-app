package pl.uginf.rcphrwebapp.hr.user.service;

import static pl.uginf.rcphrwebapp.utils.MsgCodes.NOT_UNIQUE;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.exceptions.CredentialsNotMatchException;
import pl.uginf.rcphrwebapp.exceptions.UserNotFoundException;
import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.hr.benefits.Benefit;
import pl.uginf.rcphrwebapp.hr.benefits.BenefitRecord;
import pl.uginf.rcphrwebapp.hr.benefits.BenefitRepository;
import pl.uginf.rcphrwebapp.hr.daysoff.TimeOffRepository;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.NewTimeOffRecord;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.TimeOffRecord;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.assembler.TimeOffAssembler;
import pl.uginf.rcphrwebapp.hr.daysoff.model.DaysOff;
import pl.uginf.rcphrwebapp.hr.daysoff.validator.TimeOffValidator;
import pl.uginf.rcphrwebapp.hr.user.UserRepository;
import pl.uginf.rcphrwebapp.hr.user.UserValidator;
import pl.uginf.rcphrwebapp.hr.user.dto.*;
import pl.uginf.rcphrwebapp.hr.user.model.Address;
import pl.uginf.rcphrwebapp.hr.user.model.RoleEnum;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfo;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoRepository;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoValidator;

@AllArgsConstructor(onConstructor_ = @Autowired)
@Service
public class UserServiceBean implements UserService {

    private final UserValidator userValidator;

    private final WorkInfoValidator workInfoValidator;

    private final TimeOffValidator timeOffValidator;

    private final UserRepository userRepository;

    private final WorkInfoRepository workInfoRepository;

    private final TimeOffRepository timeOffRepository;

    private final BenefitRepository benefitRepository;

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

        return allByUserId_username.stream().sorted(Comparator.comparing(WorkInfo::getTo).reversed())
                .map(workInfo -> {
                    WorkInfoDto workInfoDto = modelMapper.map(workInfo, WorkInfoDto.class);
                    workInfoDto.setUsername(workInfo.getUserId()
                            .getUsername());
                    return workInfoDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public WorkInfoDto addWorkInfo(WorkInfoDto workInfoDto) {
        workInfoValidator.validate(workInfoDto);
        WorkInfo workInfo = modelMapper.map(workInfoDto, WorkInfo.class);
        User user = getByUsername(workInfoDto.getUsername());
        workInfo.setUserId(user);
        workInfoRepository.save(workInfo);
        return workInfoDto;
    }

    @Override
    public void deactivateUser(String username) {
        User userToDeactivate = getByUsername(username);
        userToDeactivate.setActive(false);
        List<WorkInfo> workInfos = userToDeactivate.getWorkInfos();
        for (WorkInfo workInfo : workInfos) {
            if ( workInfo.getTo() == null ) {
                workInfo.setTo(new Date(new java.util.Date().getTime()));
                break;
            }
        }
        userRepository.save(userToDeactivate);
    }

    @Override
    public TimeOffRecord addDaysOffForUser(NewTimeOffRecord newTimeOff) {
        User userDb = getUserByUsername(newTimeOff.username());
        timeOffValidator.validate(newTimeOff);
        DaysOff daysOff = new DaysOff();
        daysOff.setUser(userDb);
        daysOff.setType(newTimeOff.type());
        daysOff.setApproved(false);
        daysOff.setStartDate(newTimeOff.startDate());
        daysOff.setEndDate(newTimeOff.endDate());
        timeOffRepository.save(daysOff);
        return TimeOffAssembler.assemble(daysOff);
    }

    @Override
    public List<TimeOffRecord> getDaysOffForUserBetween(String username, Date from, Date to) {
        getUserByUsername(username);
        List<DaysOff> allForUserAndBetweenPeriod = timeOffRepository.getAllForUserAndBetweenPeriod(username, from, to);
        return allForUserAndBetweenPeriod.stream()
                .map(TimeOffAssembler::assemble)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeOffRecord> getNotApprovedDaysOffForUser(String username) {
        getUserByUsername(username);
        List<DaysOff> allNotApprovedForUser = timeOffRepository.findAllByUser_UsernameAndApprovedIsFalse(username);
        return allNotApprovedForUser.stream()
                .map(TimeOffAssembler::assemble)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllTeamMembers(String managerUsername) {
        Optional<User> boss = userRepository.findUserByUsername(managerUsername);
        List<User> userList = userRepository.findAllByBoss(boss.get());
        if ( !userList.isEmpty() )
            return userList.stream()
                    .map(user -> modelMapper.map(user, UserDto.class))
                    .collect(Collectors.toList());
        else
            return Collections.emptyList();
    }

    @Override
    public void approveDaysOff(List<Long> daysOffIdList) {
        for (Long daysOffId : daysOffIdList) {
            DaysOff daysOff = timeOffRepository.getReferenceById(daysOffId);
            daysOff.setApproved(true);
            timeOffRepository.save(daysOff);
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto loginUser(LoginRecord loginRecord) {
        User user = getByUsername(loginRecord.username());
        if ( user.getPassword()
                .equals(loginRecord.password()) && user.isActive() ) {
            return modelMapper.map(user, UserDto.class);
        }
        throw new CredentialsNotMatchException();
    }

    @Override
    public List<UserDto> getAllActiveUsers() {
        List<UserDto> allUsers = getAllUsers();
        return allUsers.stream()
                .filter(UserDto::isActive)
                .toList();
    }

    @Override
    public List<BasicUserRecord> getAllManagers() {
        return userRepository.findByRoles_NameAndIsActiveTrue(RoleEnum.MANAGER)
                .stream()
                .map(user -> new BasicUserRecord(user.getUsername(), user.getFirstName(), user.getLastName()))
                .toList();
    }

    @Override
    public void addManagerForUser(String username, String managerUsername) {
        User user = getUserByUsername(username);
        User manager = getUserByUsername(managerUsername);
        user.setBoss(manager);
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserUpdateDto userUpdateDto) {
        User userToEdit = getByUsername(userUpdateDto.getUsername());
        userToEdit.setPassword(userUpdateDto.getPassword());
        userToEdit.setEmail(userUpdateDto.getEmail());
        userToEdit.setFirstName(userUpdateDto.getFirstName());
        userToEdit.setLastName(userUpdateDto.getLastName());
        userToEdit.setPhone(userUpdateDto.getPhone());
        userToEdit.setRoles(userUpdateDto.getRoles());
        AddressDto address = userUpdateDto.getAddress();
        Address dbAddress = userToEdit.getAddress();
        dbAddress.setCity(address.getCity());
        dbAddress.setCountry(address.getCountry());
        dbAddress.setStreet(address.getStreet());
        dbAddress.setHomeNumber(address.getHomeNumber());
        dbAddress.setPostalCode(address.getPostalCode());
        userRepository.save(userToEdit);
    }

    @Override
    public List<BenefitRecord> getBenefitsForUser(String username) {
        User user = getByUsername(username);
        return user.getBenefits()
                .stream()
                .map(benefit -> new BenefitRecord(benefit.getId(), benefit.getDetails(), benefit.getMonthlyCost()))
                .toList();
    }

    @Override
    public void updateBenefits(String username, List<Long> id) {
        User user = getUserByUsername(username);
        List<Benefit> benefits = benefitRepository.findByIdIn(id);
        user.setBenefits(new HashSet<>(benefits));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean changePassword(String password, String username) {
        userRepository.changePassword(password, username);
        return true;
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