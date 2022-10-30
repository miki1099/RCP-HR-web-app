package pl.uginf.rcphrwebapp.hr.workinfo;

import org.springframework.stereotype.Component;
import pl.uginf.rcphrwebapp.validator.Validator;

@Component
public class WorkInfoValidator implements Validator<WorkInfoDto> {

    @Override
    public void validate(WorkInfoDto objToValidate) {
        //TODO implement validation compare from to time, and validate to others in db
    }
}
