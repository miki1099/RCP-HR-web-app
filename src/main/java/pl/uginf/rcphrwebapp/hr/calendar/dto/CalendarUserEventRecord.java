package pl.uginf.rcphrwebapp.hr.calendar.dto;

import java.time.LocalDate;

public record CalendarUserEventRecord(String name, LocalDate from, LocalDate to, boolean approved) {

}
