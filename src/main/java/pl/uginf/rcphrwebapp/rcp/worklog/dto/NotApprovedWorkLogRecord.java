package pl.uginf.rcphrwebapp.rcp.worklog.dto;

import java.util.Date;

public record NotApprovedWorkLogRecord(long workLogId, String workLogUsername, Date from, Date to, String comment) {
}
