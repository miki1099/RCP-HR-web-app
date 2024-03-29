package pl.uginf.rcphrwebapp.hr.user.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.YearMonth;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.benefits.BenefitIdsRequest;
import pl.uginf.rcphrwebapp.hr.benefits.BenefitRecord;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.NewTimeOffRecord;
import pl.uginf.rcphrwebapp.hr.daysoff.dto.TimeOffRecord;
import pl.uginf.rcphrwebapp.hr.invoice.InvoiceService;
import pl.uginf.rcphrwebapp.hr.invoice.model.InvoiceInfoRecord;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserService;
import pl.uginf.rcphrwebapp.hr.user.summary.SummaryService;
import pl.uginf.rcphrwebapp.hr.user.summary.dto.CostSummaryRecord;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@AllArgsConstructor
@RequestMapping("/user/{username}")
public class UserController {

    private final UserService userService;

    private final InvoiceService invoiceService;

    private final SummaryService summaryService;

    @GetMapping
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.getUserDtoByUsername(username);
    }

    @PatchMapping(value = "/changePassword")
    public boolean changePassword(@RequestBody String password, @PathVariable String username) {
        return userService.changePassword(username, password);
    }

    @PutMapping(value = "/addDaysOff")
    public TimeOffRecord addTimeOff(@RequestBody NewTimeOffRecord newTimeOff) {
        return userService.addDaysOffForUser(newTimeOff);
    }

    @GetMapping(value = "/getAllDaysOffBetween")
    public List<TimeOffRecord> getDaysOffAfter(@PathVariable String username, @RequestParam("fromDate") Date from, @RequestParam("toDate") Date to) {
        return userService.getDaysOffForUserBetween(username, from, to);
    }

    @GetMapping(value = "/getNotApproved")
    public List<TimeOffRecord> getNotApprovedDaysOff(@PathVariable String username) {
        return userService.getNotApprovedDaysOffForUser(username);
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
    public ResponseEntity<byte[]> generateResource(@PathVariable String username, @RequestParam("year-month") YearMonth yearMonth) throws IOException {
        MultipartFile invoiceFile = invoiceService.generateInvoiceForUser(username, yearMonth);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = invoiceFile.getName();
        headers.setContentDispositionFormData(filename, filename);
        return new ResponseEntity<>(invoiceFile.getBytes(), headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/getBenefits")
    public List<BenefitRecord> getBenefits(@PathVariable String username) {
        return userService.getBenefitsForUser(username);
    }

    @PostMapping(value = "/updateBenefits")
    public void removeBenefit(@PathVariable String username, @RequestBody BenefitIdsRequest benefitIdsRequest) {
        userService.updateBenefits(username, benefitIdsRequest.ids());
    }

    @GetMapping(value = "/getPaymentSummary")
    public CostSummaryRecord getPaymentSummary(@PathVariable String username, @RequestParam("yearMonth") YearMonth yearMonth) {
        return summaryService.getSummaryForMonth(username, yearMonth);
    }

}
