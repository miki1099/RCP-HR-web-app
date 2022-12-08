package pl.uginf.rcphrwebapp.hr.user.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.YearMonth;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<byte[]> generateResource(@PathVariable String username, @RequestParam("month-year") YearMonth yearMonth) throws IOException {
        MultipartFile invoiceFile = invoiceService.generateInvoiceForUser(username, yearMonth);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = invoiceFile.getName();
        headers.setContentDispositionFormData(filename, filename);
        return new ResponseEntity<>(invoiceFile.getBytes(), headers, HttpStatus.CREATED);
    }

}
