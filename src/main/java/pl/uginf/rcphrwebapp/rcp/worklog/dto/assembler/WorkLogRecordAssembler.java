package pl.uginf.rcphrwebapp.rcp.worklog.dto.assembler;

import java.util.List;
import java.util.stream.Collectors;

import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

public class WorkLogRecordAssembler {

    public static WorkLogRecord assembleRecord(WorkLog workLog) {
        return new WorkLogRecord(workLog.getFrom(), workLog.getTo(), workLog.getComment(), workLog.isApproved());
    }

    public static List<WorkLogRecord> assembleMultipleRecord(List<WorkLog> workLogList) {
        return workLogList.stream()
                .map(WorkLogRecordAssembler::assembleRecord)
                .collect(Collectors.toList());
    }

}
