package pl.uginf.rcphrwebapp.hr.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public static Date getYearsLater(Date date, int years) {
        LocalDateTime localDate = getLocalDateTime(date);
        LocalDateTime dateWithMoreYears = localDate.plusYears(years);
        return Date.from(dateWithMoreYears.atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static LocalDate getLocalDate(Date date) {
        return getZonedDateTime(date).toLocalDate();
    }

    private static LocalDateTime getLocalDateTime(Date date) {
        return getZonedDateTime(date).toLocalDateTime();
    }

    private static ZonedDateTime getZonedDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault());
    }

}
