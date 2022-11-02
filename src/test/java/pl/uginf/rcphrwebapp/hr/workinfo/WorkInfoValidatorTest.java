package pl.uginf.rcphrwebapp.hr.workinfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.uginf.rcphrwebapp.hr.user.UserValidator.DATE_PATTERN;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.hr.user.UserRepository;
import pl.uginf.rcphrwebapp.hr.user.model.User;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class WorkInfoValidatorTest {

    public static final String FROM_DATE_TEST = "2020-05-20";

    public static final String TO_DATE_TEST = "2020-05-21";

    @InjectMocks
    private final WorkInfoValidator workInfoValidator = new WorkInfoValidator();

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkInfoRepository workInfoRepository;

    @Test
    public void validateValidWorkInfoTest() throws ParseException {
        //given
        WorkInfoDto workInfoDto = createWorkInfoDtoForTest();
        User user = new User();

        //when
        when(userRepository.findUserByUsername(any())).thenReturn(Optional.of(user));
        when(workInfoRepository.getAllByConflictContractDateAndUserId(any(), any(), any())).thenReturn(new ArrayList<>());
        workInfoValidator.validate(workInfoDto);

        //then
        // no validation exception test passes
    }

    @Test
    public void validateWorkInfoShouldThrowConflictContractDate() throws ParseException {
        //given
        WorkInfoDto workInfoDto = createWorkInfoDtoForTest();
        WorkInfo workInfo = new WorkInfo();

        //when
        when(userRepository.findUserByUsername(any())).thenReturn((Optional.of(new User())));
        when(workInfoRepository.getAllByConflictContractDateAndUserId(any(), any(), any())).thenReturn(List.of(workInfo));
        ValidationException error = Assertions.assertThrows(ValidationException.class, () -> workInfoValidator.validate(workInfoDto));

        //then
        assertThat(error.getMessage()).isEqualTo("There is already an agreement in the given timeframe for this user.");
    }

    @Test
    public void validateWorkInfoShouldThrowUserNotFoundException() throws ParseException {
        //given
        WorkInfoDto workInfoDto = createWorkInfoDtoForTest();

        //when
        when(userRepository.findUserByUsername(any())).thenReturn((Optional.empty()));
        ValidationException error = Assertions.assertThrows(ValidationException.class, () -> workInfoValidator.validate(workInfoDto));

        //then
        assertThat(error.getMessage()).isEqualTo("User with username usernameTest is not found.");
    }

    private WorkInfoDto createWorkInfoDtoForTest() throws ParseException {
        return WorkInfoDto.builder()
                .username("usernameTest")
                .from(new SimpleDateFormat(DATE_PATTERN).parse(FROM_DATE_TEST))
                .to(new SimpleDateFormat(DATE_PATTERN).parse(TO_DATE_TEST))
                .build();
    }

}