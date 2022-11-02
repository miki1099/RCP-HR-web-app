package pl.uginf.rcphrwebapp.rcp.worklog.dto;

import java.util.Date;

public record WorkLogBetween(String username, Date from, Date to) {
}
