package pl.uginf.rcphrwebapp.rcp.worklog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.ApproveWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.CustomWorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogBetween;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.dto.WorkLogStartRecord;
import pl.uginf.rcphrwebapp.rcp.worklog.service.WorkLogService;

@RestController
@RequestMapping("/workLog")
@AllArgsConstructor
public class WorkLogController {

    WorkLogService workLogService;

    @PutMapping("/startWorkLog")
    public WorkLogStartRecord startWorkLog(@RequestParam String username) {
        return workLogService.startWork(username);
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
    public List<WorkLogRecord> getWorkLogList(@RequestBody WorkLogBetween workLogBetween) {
        return workLogService.getAllForUserBetween(workLogBetween);
    }

}
