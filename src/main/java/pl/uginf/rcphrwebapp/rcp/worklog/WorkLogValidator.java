package pl.uginf.rcphrwebapp.rcp.worklog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.CustomWorkLogRecord;
import pl.uginf.rcphrwebapp.utils.MsgCodes;
import pl.uginf.rcphrwebapp.validator.Validator;

@Component
public class WorkLogValidator implements Validator<CustomWorkLogRecord> {

    @Override
    public void validate(CustomWorkLogRecord workLog) {
        List<String> errors = new ArrayList<>();
        checkWorkLogDates(workLog, errors);
        if ( CollectionUtils.isNotEmpty(errors) ) {
            throw new ValidationException(errors);
        }
    }

    private void checkWorkLogDates(CustomWorkLogRecord workLog, List<String> errors) {
        Date workLogFrom = workLog.from();
        Date workLogTo = workLog.to();
        boolean isFromNull = workLogFrom == null;
        boolean isToNull = workLogTo == null;
        if ( isFromNull ) {
            errors.add(MsgCodes.ERROR_EMPTY.getMsg("Work date from"));
        }
        if ( isToNull ) {
            errors.add(MsgCodes.ERROR_EMPTY.getMsg("Work date to"));
        }
        if ( !isToNull && !isFromNull && !workLogFrom.before(workLogTo) ) {
            errors.add(MsgCodes.BEFORE_ERROR.getMsg("Work date to", "Work date from"));
        }
    }
}
