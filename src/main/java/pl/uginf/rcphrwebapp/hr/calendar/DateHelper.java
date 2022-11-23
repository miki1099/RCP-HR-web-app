package pl.uginf.rcphrwebapp.hr.calendar;

import java.time.LocalDate;
import java.time.YearMonth;
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

    public static Date getYearsLater(Date date, int years) {
        LocalDate localDate = getLocalDate(date);
        LocalDate dateWithMoreYears = localDate.plusYears(years);
        return Date.from(dateWithMoreYears.atStartOfDay(ZoneId.systemDefault())
                .toInstant());
    }

    public static LocalDate getLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDate getFirstDayOfNextMonth(YearMonth month) {
        return month.atEndOfMonth()
                .plusDays(1);
    }
}
