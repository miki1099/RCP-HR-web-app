package pl.uginf.rcphrwebapp.rcp.worklog.dto.assembler;

import java.util.List;
import java.util.stream.Collectors;

import pl.uginf.rcphrwebapp.rcp.worklog.dto.NotApprovedWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

public class NotApprovedWorkLogAssembler {
    public static NotApprovedWorkLogRecord assembleNotApprovedRecord(WorkLog workLog) {
        return new NotApprovedWorkLogRecord(workLog.getId(), workLog.getUser()
                .getUsername(), workLog.getFrom(), workLog.getTo(), workLog.getComment());
    }

    public static List<NotApprovedWorkLogRecord> assembleNotApprovedRecordList(List<WorkLog> workLogList) {
        return workLogList.stream()
                .map(NotApprovedWorkLogAssembler::assembleNotApprovedRecord)
                .collect(Collectors.toList());
    }
}
