package pl.uginf.rcphrwebapp.hr.user.controller;

import java.sql.Date;
import java.time.YearMonth;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.NewTimeOffRecord;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.TimeOffRecord;
import pl.uginf.rcphrwebapp.hr.invoice.InvoiceService;
import pl.uginf.rcphrwebapp.hr.invoice.model.InvoiceInfoRecord;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;

@RestController
@AllArgsConstructor
@RequestMapping("/user/{username}")
public class UserController {

    private final UserSLO userSLO;

    private final InvoiceService invoiceService;

    @GetMapping
    public UserDto getUserByUsername(@PathVariable String username) {
        return userSLO.getUserDtoByUsername(username);
    }

    @PutMapping(value = "/addDaysOff")
    public TimeOffRecord addTimeOff(@RequestBody NewTimeOffRecord newTimeOff) {
        return userSLO.addDaysOffForUser(newTimeOff);
    }

    @GetMapping(value = "/getAllDaysOffBetween")
    public List<TimeOffRecord> getDaysOffAfter(@PathVariable String username, @RequestParam("fromDate") Date from, @RequestParam("toDate") Date to) {
        return userSLO.getDaysOffForUserBetween(username, from, to);
    }

    @GetMapping(value = "/getNotApproved")
    public List<TimeOffRecord> getNotApprovedDaysOff(@PathVariable String username) {
        return userSLO.getNotApprovedDaysOffForUser(username);
    }

    @GetMapping(value = "/getInvoiceInfo")
    public InvoiceInfoRecord getInvoiceInfo(@PathVariable String username) {
        return invoiceService.getInvoiceInfoForUser(username);
    }

    @PutMapping(value = "/addInvoiceInfo")
    public InvoiceInfoRecord addInvoiceForUser(@PathVariable String username, @RequestBody InvoiceInfoRecord invoiceInfoRecord) {
        return invoiceService.setInvoiceInfoForUser(username, invoiceInfoRecord);
    }

    @GetMapping(value = "/generateInvoice")
    public Resource generateResource(@PathVariable String username, @RequestParam("month-year") YearMonth yearMonth) {
        return invoiceService.generateInvoiceForUser(username, yearMonth);
    }

}
