package pl.uginf.rcphrwebapp.rcp.worklog.service;

import static pl.uginf.rcphrwebapp.utils.MsgCodes.WORK_LOG_NOT_STARTED;
import static pl.uginf.rcphrwebapp.utils.MsgCodes.WORK_LOG_STARTED;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.exceptions.NotFoundException;
import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;
import pl.uginf.rcphrwebapp.rcp.worklog.WorkLogRepository;
import pl.uginf.rcphrwebapp.rcp.worklog.WorkLogValidator;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.ApproveWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.CustomWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogBetween;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogStartRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.assembler.WorkLogRecordAssembler;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLogFlag;
import pl.uginf.rcphrwebapp.utils.MsgCodes;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class WorkLogServiceBean implements WorkLogService {

    private final WorkLogRepository workLogRepository;

    private final WorkLogValidator workLogValidator;

    private final UserSLO userSLO;

    @Override
    public WorkLogStartRecord startWork(String username) {
        User user = userSLO.getUserByUsername(username);
        getWorkLogWithCustomExceptionMsg(WORK_LOG_STARTED, username);
        WorkLog workLog = new WorkLog();
        workLog.setFrom(new Date());
        workLog.setApproved(true);
        workLog.setStatus(WorkLogFlag.NOT_FINISHED);
        workLog.setUser(user);
        workLogRepository.save(workLog);
        return new WorkLogStartRecord(workLog.getFrom(), username);
    }

    @Override
    public WorkLogRecord endWork(String username) {
        userSLO.getUserByUsername(username);
        WorkLog workLogStarted = getWorkLogWithCustomExceptionMsg(WORK_LOG_NOT_STARTED, username);
        workLogStarted.setTo(new Date());
        workLogStarted.setStatus(null);
        return new WorkLogRecord(workLogStarted.getFrom(), workLogStarted.getTo(), workLogStarted.getComment(), workLogStarted.isApproved());
    }

    @Override
    public WorkLogRecord addCustomWorkLog(CustomWorkLogRecord customWorkLog) {
        User user = userSLO.getUserByUsername(customWorkLog.username());
        workLogValidator.validate(customWorkLog);
        WorkLog workLog = new WorkLog();
        workLog.setFrom(customWorkLog.from());
        workLog.setTo(customWorkLog.to());
        workLog.setComment(customWorkLog.comment());
        workLog.setApproved(false);
        workLog.setUser(user);
        workLogRepository.save(workLog);
        return WorkLogRecordAssembler.assembleRecord(workLog);
    }

    @Override
    public List<WorkLogRecord> getAllForUserBetween(WorkLogBetween workLogBetween) {
        String username = workLogBetween.username();
        userSLO.getUserByUsername(username);
        List<WorkLog> workLogs = workLogRepository.findByToAfterAndFromBeforeAndUser_UsernameOrderByFromAsc(workLogBetween.to(), workLogBetween.from(),
                username);
        return workLogs.stream()
                .map(WorkLogRecordAssembler::assembleRecord)
                .collect(Collectors.toList());
    }

    @Override
    public WorkLogRecord approveRecord(ApproveWorkLogRecord approveWorkLogRecord) {
        long workLogId = approveWorkLogRecord.workLogId();
        WorkLog workLog = workLogRepository.findById(workLogId)
                .orElseThrow(() -> new NotFoundException("WorkLog with id " + workLogId));
        workLog.setApproved(true);
        return WorkLogRecordAssembler.assembleRecord(workLog);
    }

    private WorkLog getWorkLogWithCustomExceptionMsg(MsgCodes exceptionMsg, String username) {
        Optional<WorkLog> workLogStarted = workLogRepository.findByStatusNotNullAndUser_Username(username);
        return workLogStarted.orElseThrow(() -> new ValidationException(exceptionMsg.getMsg(username)));
    }
}
