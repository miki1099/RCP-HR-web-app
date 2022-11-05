package pl.uginf.rcphrwebapp.hr.daysoff.dto;

import java.sql.Date;

import pl.uginf.rcphrwebapp.hr.daysoff.model.DayOffType;

public record TimeOffRecord(long id, Date startDate, Date endDate, DayOffType type, boolean approved, String username) {
}
