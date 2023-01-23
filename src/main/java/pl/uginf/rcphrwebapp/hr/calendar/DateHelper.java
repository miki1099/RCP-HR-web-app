package pl.uginf.rcphrwebapp.hr.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        if(date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }

        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime getLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDate getFirstDayOfNextMonth(YearMonth month) {
        return month.atEndOfMonth()
                .plusDays(1);
    }

    public static double getHoursBetweenForSecondDate(Date date1Start, Date date1End, Date date2Start, Date date2End) {
        boolean date1StartBeforeDate2Start = date1Start.before(date2Start);
        boolean date1EndAfterDate2End = date1End == null || date1End.after(date2End);
        if ( date1StartBeforeDate2Start && date1EndAfterDate2End ) {
            return hoursBetween(date2Start, date2End);
        } else if ( date1StartBeforeDate2Start ) {
            return hoursBetween(date2Start, date1End);
        } else if ( date1EndAfterDate2End ) {
            return hoursBetween(date1Start, date2End);
        } else {
            return 0;
        }
    }

    private static double hoursBetween(Date start, Date stop) {
        long diff = stop.getTime() - start.getTime();
        return diff / (1000d * 60 * 60);
    }
}
