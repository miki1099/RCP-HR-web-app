package pl.uginf.rcphrwebapp.rcp.worklog.service;

import static pl.uginf.rcphrwebapp.rcp.worklog.dto.assembler.NotApprovedWorkLogAssembler.assembleNotApprovedRecordList;
import static pl.uginf.rcphrwebapp.rcp.worklog.dto.assembler.WorkLogRecordAssembler.assembleMultipleRecord;
import static pl.uginf.rcphrwebapp.rcp.worklog.dto.assembler.WorkLogRecordAssembler.assembleRecord;
import static pl.uginf.rcphrwebapp.utils.MsgCodes.WORK_LOG_STARTED;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.exceptions.NotFoundException;
import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.hr.user.service.UserService;
import pl.uginf.rcphrwebapp.rcp.worklog.WorkLogRepository;
import pl.uginf.rcphrwebapp.rcp.worklog.WorkLogValidator;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.ApproveWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.CustomWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.NotApprovedWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogBetween;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogDTO;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogStartRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLogFlag;
import pl.uginf.rcphrwebapp.utils.MsgCodes;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class WorkLogServiceBean implements WorkLogService {

    private final WorkLogRepository workLogRepository;

    private final WorkLogValidator workLogValidator;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Override
    public WorkLogStartRecord startWork(String username) {
        User user = userService.getUserByUsername(username);
        isWorkAlreadyStaredWithCustomExceptionMsg(username);

        WorkLogDTO workLogDTO = WorkLogDTO.builder()
                .from(new Date())
                .isApproved(true)
                .status(WorkLogFlag.NOT_FINISHED)
                .user(user)
                .build();

        workLogRepository.save(modelMapper.map(workLogDTO, WorkLog.class));
        return new WorkLogStartRecord(workLogDTO.getFrom(), username);
    }

    @Override
    public Boolean isStarted(String username) {
        return workLogRepository.findByStatusNotNullAndUser_Username(username).isPresent();
    }

    @Override
    @Transactional
    public WorkLogRecord endWork(String username) {
        userService.getUserByUsername(username);
        WorkLog workLogStarted = getStartedWorkLogWithCustomExceptionMsg(username);
        workLogStarted.setTo(new Date());
        workLogStarted.setStatus(null);
        return new WorkLogRecord(workLogStarted.getFrom(), workLogStarted.getTo(), workLogStarted.getComment(), workLogStarted.isApproved());
    }

    @Override
    public WorkLogRecord addCustomWorkLog(CustomWorkLogRecord customWorkLog) {
        User user = userService.getUserByUsername(customWorkLog.username());
        workLogValidator.validate(customWorkLog);
        WorkLog workLog = new WorkLog();
        workLog.setFrom(customWorkLog.from());
        workLog.setTo(customWorkLog.to());
        workLog.setComment(customWorkLog.comment());
        workLog.setApproved(false);
        workLog.setUser(user);
        workLogRepository.save(workLog);
        return assembleRecord(workLog);
    }

    @Override
    public List<WorkLogRecord> getAllForUserBetween(WorkLogBetween workLogBetween) {
        String username = workLogBetween.username();
        userService.getUserByUsername(username);
        List<WorkLog> workLogs = workLogRepository.findAllByBetweenFromAndToAndUserId(username, workLogBetween.from(), workLogBetween.to());
        return assembleMultipleRecord(workLogs);
    }

    @Override
    @Transactional
    public List<WorkLogRecord> approveRecord(List<ApproveWorkLogRecord> approveWorkLogRecordList) {
        List<WorkLogRecord> workLogRecordList = new ArrayList<>();
        for (ApproveWorkLogRecord approveWorkLogRecord : approveWorkLogRecordList) {
            long workLogId = approveWorkLogRecord.workLogId();
            WorkLog workLog = workLogRepository.findById(workLogId)
                    .orElseThrow(() -> new NotFoundException("WorkLog with id " + workLogId));
            workLog.setApproved(true);
            workLogRepository.save(workLog);
            workLogRecordList.add(assembleRecord(workLog));
        }
        return workLogRecordList;
    }

    public List<NotApprovedWorkLogRecord> getNotApprovedRecord(String managerUsername) {
        List<UserDto> userList = userService.getAllTeamMembers(managerUsername);
        List<WorkLog> workLogs = new ArrayList<>();
        for (UserDto userDto : userList) {
            workLogs.addAll(workLogRepository.findAllByIsApprovedFalseAndUser_Username(userDto.getUsername()));
        }
        return assembleNotApprovedRecordList(workLogs);
    }

    private WorkLog getStartedWorkLogWithCustomExceptionMsg(String username) {
        Optional<WorkLog> workLogStarted = workLogRepository.findByStatusNotNullAndUser_Username(username);
        return workLogStarted.orElseThrow(() -> new ValidationException(MsgCodes.WORK_LOG_NOT_STARTED.getMsg(username)));
    }

    private void isWorkAlreadyStaredWithCustomExceptionMsg(String username) {
        Optional<WorkLog> workLogStarted = workLogRepository.findByStatusNotNullAndUser_Username(username);
        if ( workLogStarted.isPresent() ) {
            throw new ValidationException(WORK_LOG_STARTED.getMsg(username));
        }
    }
}
