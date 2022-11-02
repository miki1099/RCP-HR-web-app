package pl.uginf.rcphrwebapp.rcp.worklog.dto.assembler;

import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

public class WorkLogRecordAssembler {

    public static WorkLogRecord assembleRecord(WorkLog workLog) {
        return new WorkLogRecord(workLog.getFrom(), workLog.getTo(), workLog.getComment(), workLog.isApproved());
    }

}
