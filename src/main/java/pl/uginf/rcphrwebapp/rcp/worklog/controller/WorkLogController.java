package pl.uginf.rcphrwebapp.rcp.worklog.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.ApproveWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.CustomWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.NotApprovedWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogBetween;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogStartRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.service.WorkLogService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/workLog")
@AllArgsConstructor
public class WorkLogController {

    WorkLogService workLogService;

    @PutMapping("/startWorkLog")
    public WorkLogStartRecord startWorkLog(@RequestParam String username) {
        return workLogService.startWork(username);
    }

    @GetMapping("/isStarted")
    public Boolean isStarted(@RequestParam String username) {
        return workLogService.isStarted(username);
    }

    @PostMapping("/endWorkLog")
    public WorkLogRecord endWorkLog(@RequestParam String username) {
        return workLogService.endWork(username);
    }

    @PutMapping("/createWorkLog")
    public WorkLogRecord createCustomWorkLog(@RequestBody CustomWorkLogRecord customWorkLogRecord) {
        return workLogService.addCustomWorkLog(customWorkLogRecord);
    }

    @PostMapping("/approveWorkLog")
    public List<WorkLogRecord> approveWorkLog(@RequestBody List<ApproveWorkLogRecord> approveWorkLogRecordList) {
        return workLogService.approveRecord(approveWorkLogRecordList);
    }

    @GetMapping("/getWorkLogForUser")
    public List<WorkLogRecord> getWorkLogList(@RequestParam String username, @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        return workLogService.getAllForUserBetween(new WorkLogBetween(username, from, to));
    }

    @GetMapping(value = "/not-approved-work-log")
    public List<NotApprovedWorkLogRecord> getNotApprovedWorkLog(@RequestParam String managerUsername) {
        return workLogService.getNotApprovedRecord(managerUsername);
    }

}
