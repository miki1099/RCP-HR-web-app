package pl.uginf.rcphrwebapp.hr.user.summary;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uginf.rcphrwebapp.exceptions.InvoiceGenerationException;
import pl.uginf.rcphrwebapp.hr.benefits.BenefitRecord;
import pl.uginf.rcphrwebapp.hr.calendar.DateHelper;
import pl.uginf.rcphrwebapp.hr.invoice.InvoiceService;
import pl.uginf.rcphrwebapp.hr.invoice.model.InvoiceInfoRecord;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.hr.user.service.UserService;
import pl.uginf.rcphrwebapp.hr.user.summary.dto.CostSummaryRecord;
import pl.uginf.rcphrwebapp.hr.user.summary.dto.TheoreticalSummaryRecord;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfo;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class SummaryService {

    private final UserService userService;

    private final InvoiceService invoiceService;

    public CostSummaryRecord getSummaryForMonth(String username, YearMonth month) {
        User user = userService.getUserByUsername(username);
        int workedMinutes = getWorkedMinutes(user, month);
        TheoreticalSummaryRecord theoreticalSummaryRecord = getPaymentSummary(user, month);
        return new CostSummaryRecord(workedMinutes, theoreticalSummaryRecord, getInvoiceInfo(username));
    }

    private TheoreticalSummaryRecord getPaymentSummary(User user, YearMonth month) {
        int daysInMonth = month.lengthOfMonth();

        LocalDate fromLimit = month.atDay(1).minusDays(1);
        LocalDate toLimit = month.atEndOfMonth().plusDays(1);

        List<WorkInfo> workInfosInMonth = user.getWorkInfos().stream()
                .filter(workInfo -> fromLimit.isBefore(DateHelper.getLocalDate(workInfo.getFrom())) || toLimit.isAfter(DateHelper.getLocalDate(workInfo.getTo()))).toList();

        List<BenefitRecord> userBenefits = userService.getBenefitsForUser(user.getUsername());
        if(CollectionUtils.isEmpty(workInfosInMonth)) {
            return new TheoreticalSummaryRecord(BigDecimal.ZERO, 0, userBenefits);
        }

        BigDecimal earn = BigDecimal.ZERO;
        int minutes = 0;

        LocalDate nowLimit = LocalDate.now().plusDays(1);

        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate currentDate = month.atDay(i);
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                continue;
            }

            Optional<WorkInfo> assignedWorkInfo = workInfosInMonth.stream().filter(workInfo -> checkInRange(workInfo, currentDate)).findFirst();
            if(assignedWorkInfo.isPresent()) {
                WorkInfo workInfo = assignedWorkInfo.get();
                short dailyWorkingTime = workInfo.getDailyWorkingTime();
                earn = earn.add(workInfo.getHourlyRate().multiply(BigDecimal.valueOf(dailyWorkingTime)));
                if(nowLimit.isAfter(currentDate)) {
                    minutes += (int) TimeUnit.HOURS.toMinutes(dailyWorkingTime);
                }
            }
        }

        return new TheoreticalSummaryRecord(earn, minutes, userBenefits);
    }

    private boolean checkInRange(WorkInfo workInfo, LocalDate date) {
        return !(date.isBefore(DateHelper.getLocalDate(workInfo.getFrom())) || date.isAfter(DateHelper.getLocalDate(workInfo.getTo())));
    }

    private int getWorkedMinutes(User user, YearMonth month) {
        LocalDateTime fromLimit = month.atDay(1).atTime(0,0);
        LocalDateTime toLimit = month.atEndOfMonth().plusDays(1).atTime(23,59);

        int minutes = 0;

        List<WorkLog> workLogs = user.getWorkLogs().stream().filter(workLog -> workLog.getTo() != null)
                .filter(workLog -> fromLimit.isBefore(DateHelper.getLocalDateTime(workLog.getTo())))
                .filter(workLog -> toLimit.isAfter(DateHelper.getLocalDateTime(workLog.getFrom()))).toList();

        for (WorkLog worklog : workLogs) {
            LocalDateTime start = DateHelper.getLocalDateTime(worklog.getFrom());
            LocalDateTime stop = DateHelper.getLocalDateTime(worklog.getTo());

            if(fromLimit.isAfter(start)) {
                start = fromLimit;
            }
            if(toLimit.isBefore(stop)) {
                stop = toLimit;
            }

            minutes += ChronoUnit.MINUTES.between(start, stop);
        }
        return minutes;
    }

    private InvoiceInfoRecord getInvoiceInfo(String username) {
        try {
            return invoiceService.getInvoiceInfoForUser(username);
        } catch (InvoiceGenerationException ignore) {
            return null;
        }
    }

}
