package pl.uginf.rcphrwebapp.hr.invoice;

import static pl.uginf.rcphrwebapp.hr.calendar.DateHelper.getFirstDayOfNextMonth;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.uginf.rcphrwebapp.exceptions.InvoiceGenerationException;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InvoiceService {

    private final UserSLO userSLO;

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM.yyyy");

    public Resource generateInvoiceForUser(String username, YearMonth monthForInvoice) {
        List<WorkInfoDto> workInfosBetween = getWorkInfosForMonth(username, monthForInvoice);

        if ( CollectionUtils.isEmpty(workInfosBetween) ) {
            throw new InvoiceGenerationException(
                    "User %s for month %s does not have any work contract with hourly rate.".formatted(username, monthForInvoice.format(dateTimeFormatter)));
        }

        List<WorkLog> workLogsForUser = getWorkLogsForMonth(username, monthForInvoice);

        //TODO end sum salary and generate Invoice
        return null;
    }

    private List<WorkInfoDto> getWorkInfosForMonth(String username, YearMonth month) {
        List<WorkInfoDto> workInfos = userSLO.getWorkInfosForUser(username);
        LocalDate fromFilter = month.atDay(1);
        LocalDate toFilter = getFirstDayOfNextMonth(month);
        return workInfos.stream()
                .filter(workInfo -> fromFilter.isBefore(workInfo.getTo()
                        .toLocalDate()))
                .filter(workInfo -> toFilter.isAfter(workInfo.getFrom()
                        .toLocalDate()))
                .filter(workInfo -> workInfo.getHourlyRate() != null)
                .sorted(Comparator.comparing(WorkInfoDto::getFrom))
                .toList();
    }

    private List<WorkLog> getWorkLogsForMonth(String username, YearMonth month) {
        return userSLO.getWorkLogsForUser(username, Date.valueOf(month.atDay(1)), Date.valueOf(getFirstDayOfNextMonth(month)));
    }

}
