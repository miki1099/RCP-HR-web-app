package pl.uginf.rcphrwebapp.rcp.worklog.service;

import java.util.List;

import pl.uginf.rcphrwebapp.rcp.worklog.dto.ApproveWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.CustomWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogBetween;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogStartRecord;

public interface WorkLogService {

    WorkLogStartRecord startWork(String username);

    WorkLogRecord endWork(String username);

    WorkLogRecord addCustomWorkLog(CustomWorkLogRecord customWorkLog);

    List<WorkLogRecord> getAllForUserBetween(WorkLogBetween workLogBetween);

    WorkLogRecord approveRecord(ApproveWorkLogRecord approveWorkLogRecord);
}
