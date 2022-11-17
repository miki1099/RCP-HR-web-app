package pl.uginf.rcphrwebapp.hr.calendar;

import static pl.uginf.rcphrwebapp.hr.calendar.DateHelper.getOldDateShiftedToCurrent;
import static pl.uginf.rcphrwebapp.hr.calendar.DateHelper.getYearsToNow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.calendar.dto.CalendarEventRecord;
import pl.uginf.rcphrwebapp.hr.calendar.holiday.CountryHolidayService;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class CalendarService {

    private final UserSLO userSlo;

    private final CountryHolidayService countryHolidayService;

    public List<CalendarEventRecord> getEventsForAllUsers() {
        List<UserDto> allUsers = userSlo.getAllUsers();
        List<CalendarEventRecord> events = new ArrayList<>(getAllUsersBirthday(allUsers));
        events.addAll(getWorkAnniversary(allUsers));
        events.addAll(countryHolidayService.getHolidaysForCountry("PL")); //TODO other countries
        return sortCalendarByDates(events);
    }

    private List<CalendarEventRecord> sortCalendarByDates(List<CalendarEventRecord> events) {
        return events.stream()
                .sorted(Comparator.comparing(CalendarEventRecord::date))
                .collect(Collectors.toList());
    }

    private Set<CalendarEventRecord> getHolidays() {
        return null;
    }

    private Set<CalendarEventRecord> getAllUsersBirthday(List<UserDto> allUsers) {
        Function<UserDto, CalendarEventRecord> map = user -> new CalendarEventRecord(getBirthdayLabel(user), getOldDateShiftedToCurrent(user.getBirthDate()));
        return mapAllUsers(allUsers, map);
    }

    private String getBirthdayLabel(UserDto userDto) {
        return getNameLabel(userDto) + " birthday";
    }

    private Set<CalendarEventRecord> getWorkAnniversary(List<UserDto> allUsers) {
        Function<UserDto, CalendarEventRecord> map = user -> new CalendarEventRecord(getWorkAnniversaryLabel(user),
                getOldDateShiftedToCurrent(user.getHireDate()));
        return mapAllUsers(allUsers, map);
    }

    private Set<CalendarEventRecord> mapAllUsers(List<UserDto> allUsers, Function<UserDto, CalendarEventRecord> map) {
        return allUsers.stream()
                .map(map)
                .collect(Collectors.toSet());
    }

    private String getWorkAnniversaryLabel(UserDto userDto) {
        long year = getYearsToNow(userDto.getHireDate());
        return getNameLabel(userDto) + StringUtils.SPACE + year + " work anniversary";
    }

    private String getNameLabel(UserDto userDto) {
        return userDto.getFirstName() + StringUtils.SPACE + userDto.getLastName();
    }
}
