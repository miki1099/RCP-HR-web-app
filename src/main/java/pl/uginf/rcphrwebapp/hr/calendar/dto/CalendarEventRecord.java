package pl.uginf.rcphrwebapp.hr.calendar.dto;

import java.time.LocalDate;

public record CalendarEventRecord(String name, LocalDate date) {
}
