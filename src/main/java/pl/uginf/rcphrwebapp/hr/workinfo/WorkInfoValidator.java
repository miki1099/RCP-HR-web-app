package pl.uginf.rcphrwebapp.hr.workinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.hr.user.UserRepository;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.utils.MsgCodes;
import pl.uginf.rcphrwebapp.validator.Validator;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
@NoArgsConstructor
public class WorkInfoValidator implements Validator<WorkInfoDto> {

    public static final int FROM_IS_EARLIER = -1;

    private WorkInfoRepository workInfoRepository;

    private UserRepository userRepository;

    @Override
    public void validate(WorkInfoDto objToValidate) {
        List<String> errors = new ArrayList<>();
        compareStartAndEndContract(objToValidate.getFrom(), objToValidate.getTo(), errors);
        ifUserHasConflictDateWithOtherContract(objToValidate.getFrom(), objToValidate.getTo(), objToValidate.getUsername(), errors);
        if ( CollectionUtils.isNotEmpty(errors) ) {
            throw new ValidationException(errors);
        }
    }

    private void compareStartAndEndContract(Date from, Date to, List<String> errors) {
        if ( from.compareTo(to) > FROM_IS_EARLIER ) {
            errors.add(MsgCodes.INCORRECT_DATE_VALUE.getMsg(to.toString()));
        }
    }

    private void ifUserHasConflictDateWithOtherContract(Date from, Date to, String username, List<String> errors) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if ( user.isEmpty() ) {
            errors.add(MsgCodes.USER_NOT_FOUND.getMsg(username));
            return;
        }
        List<WorkInfo> workInfoList = workInfoRepository.getAllByConflictContractDateAndUserId(from, to, user.get());
        if ( CollectionUtils.isNotEmpty(workInfoList) ) {
            errors.add(MsgCodes.CONFLICT_CONTRACT_DATE.getMsg());
        }
    }

}
