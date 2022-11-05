package pl.uginf.rcphrwebapp.hr.daysoff.dto;

import java.sql.Date;

import pl.uginf.rcphrwebapp.hr.daysoff.model.DayOffType;

public record NewTimeOffRecord(Date startDate, Date endDate, DayOffType type, String username) {
}
