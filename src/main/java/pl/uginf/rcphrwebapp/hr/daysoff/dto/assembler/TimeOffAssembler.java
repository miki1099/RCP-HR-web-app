package pl.uginf.rcphrwebapp.hr.daysoff.dto.assembler;

import java.sql.Date;

import lombok.experimental.UtilityClass;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.TimeOffRecord;
import pl.uginf.rcphrwebapp.hr.daysoff.model.DaysOff;

@UtilityClass
public class TimeOffAssembler {

    public static TimeOffRecord assemble(DaysOff daysOff) {
        return new TimeOffRecord(daysOff.getId(), new Date(daysOff.getStartDate()
                .getTime()), new Date(daysOff.getEndDate()
                .getTime()), daysOff.getType(), daysOff.isApproved(), daysOff.getUser()
                .getUsername());
    }
}
