package pl.uginf.rcphrwebapp.hr.calendar;

import static pl.uginf.rcphrwebapp.hr.calendar.DateHelper.getOldDateShiftedToCurrent;
import static pl.uginf.rcphrwebapp.hr.calendar.DateHelper.getYearsLater;
import static pl.uginf.rcphrwebapp.hr.calendar.DateHelper.getYearsToNow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.calendar.dto.CalendarEventRecord;
import pl.uginf.rcphrwebapp.hr.calendar.dto.CalendarUserEventRecord;
import pl.uginf.rcphrwebapp.hr.calendar.holiday.CountryHolidayService;
import pl.uginf.rcphrwebapp.hr.daysoff.model.DaysOff;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.hr.user.service.UserService;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class CalendarService {

    private final static int YEAR_LATER = 1;

    private final UserService userService;

    private final CountryHolidayService countryHolidayService;

    public List<CalendarUserEventRecord> getEventsForUser(String username) {
        User user = userService.getUserByUsername(username);
        List<DaysOff> daysOffList = user.getDaysOffList();
        Date now = new Date();
        Date yearLater = getYearsLater(now, YEAR_LATER);
        return daysOffList.stream()
                .filter(daysOff -> now.before(daysOff.getEndDate()))
                .filter(daysOff -> yearLater.after(daysOff.getStartDate()))
                .sorted(Comparator.comparing(DaysOff::getStartDate))
                .map(daysOff -> new CalendarUserEventRecord(daysOff.getType()
                        .getName(), daysOff.getStartDate()
                        .toLocalDate(), daysOff.getEndDate()
                        .toLocalDate(), daysOff.isApproved()))
                .collect(Collectors.toList());
    }

    public List<CalendarEventRecord> getEventsForAllUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
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
