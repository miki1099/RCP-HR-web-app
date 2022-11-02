package pl.uginf.rcphrwebapp.rcp.worklog.dto;

import java.util.Date;

public record CustomWorkLogRecord(String username, Date from, Date to, String comment) {
}
