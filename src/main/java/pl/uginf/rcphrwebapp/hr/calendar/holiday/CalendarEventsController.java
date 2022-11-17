package pl.uginf.rcphrwebapp.hr.calendar.holiday;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.calendar.CalendarService;
import pl.uginf.rcphrwebapp.hr.calendar.dto.CalendarEventRecord;

@RestController
@RequestMapping("/user/calendar")
@AllArgsConstructor
public class CalendarEventsController {

    private final CalendarService calendarService;

    @GetMapping("/eventsForAll")
    public List<CalendarEventRecord> getEventsForAllUsers() {
        return calendarService.getEventsForAllUsers();
    }
}
