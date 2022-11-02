package pl.uginf.rcphrwebapp.rcp.worklog;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.CustomWorkLogRecord;

class WorkLogValidatorTest {

    private final WorkLogValidator workLogValidator = new WorkLogValidator();

    @Test
    public void validateWorkLog() {
        //given
        CustomWorkLogRecord workLogRecord = createCustomWorkLogRecord(true);

        //when
        workLogValidator.validate(workLogRecord);

        //then
        // no validation exception test passes
    }

    @Test
    public void validateInvalidWorkLog() {
        //given
        CustomWorkLogRecord workLogRecord = createCustomWorkLogRecord(false);

        //when
        ValidationException error = Assertions.assertThrows(ValidationException.class, () -> workLogValidator.validate(workLogRecord));

        //then
        assertThat(error.getMessage()).isEqualTo("Work date to can not be before Work date from");
    }

    private CustomWorkLogRecord createCustomWorkLogRecord(boolean valid) {
        long fromMilis = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
        if ( valid ) {
            fromMilis -= TimeUnit.HOURS.toMillis(8);
        }
        Date from = new Date(fromMilis);
        Date to = new Date();
        return new CustomWorkLogRecord("username", from, to, null);
    }
}