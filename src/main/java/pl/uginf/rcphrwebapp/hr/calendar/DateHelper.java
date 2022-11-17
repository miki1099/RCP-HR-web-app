package pl.uginf.rcphrwebapp.hr.calendar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateHelper {

    public static LocalDate getOldDateShiftedToCurrent(Date oldDate) {
        LocalDate old = getLocalDate(oldDate);
        return old.plusYears(getYearsToNow(oldDate));
    }

    public static long getYearsToNow(Date date) {
        LocalDate old = getLocalDate(date);
        return ChronoUnit.YEARS.between(old, LocalDate.now()) + 1;
    }

    public static LocalDate getLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}
