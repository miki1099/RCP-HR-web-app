package pl.uginf.rcphrwebapp.rcp.worklog.dto;

import java.util.Date;

public record WorkLogRecord(long id, Date from, Date to, String comment, boolean isApproved) {
}
