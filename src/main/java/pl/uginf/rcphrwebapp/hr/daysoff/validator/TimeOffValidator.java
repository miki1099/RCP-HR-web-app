package pl.uginf.rcphrwebapp.hr.daysoff.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.NewTimeOffRecord;
import pl.uginf.rcphrwebapp.utils.MsgCodes;
import pl.uginf.rcphrwebapp.validator.Validator;

@Component
public class TimeOffValidator implements Validator<NewTimeOffRecord> {

    @Override
    public void validate(NewTimeOffRecord objToValidate) {
        List<String> errors = new ArrayList<>();
        boolean startDateNull = objToValidate.startDate() == null;
        boolean endDateNull = objToValidate.endDate() == null;
        if ( startDateNull ) {
            errors.add(MsgCodes.ERROR_EMPTY.getMsg("Start date"));
        }
        if ( endDateNull ) {
            errors.add(MsgCodes.ERROR_EMPTY.getMsg("End date"));
        }

        if ( !startDateNull && !endDateNull && objToValidate.startDate()
                .after(objToValidate.endDate()) ) {
            errors.add(MsgCodes.BEFORE_ERROR.getMsg("End date", "Start date"));
        }
        if ( CollectionUtils.isNotEmpty(errors) ) {
            throw new ValidationException(errors);
        }
    }
}
