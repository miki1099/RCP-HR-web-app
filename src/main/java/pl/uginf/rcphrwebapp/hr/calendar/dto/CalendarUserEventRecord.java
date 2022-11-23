package pl.uginf.rcphrwebapp.hr.calendar.dto;

import java.util.Date;

public record CalendarUserEventRecord(String name, Date from, Date to, boolean approved) {

}
