package pl.uginf.rcphrwebapp.hr.invoice;

import static pl.uginf.rcphrwebapp.hr.calendar.DateHelper.getFirstDayOfNextMonth;
import static pl.uginf.rcphrwebapp.hr.calendar.DateHelper.getHoursBetweenForSecondDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pl.uginf.rcphrwebapp.exceptions.InvoiceGenerationException;
import pl.uginf.rcphrwebapp.hr.invoice.generator.InvoiceData;
import pl.uginf.rcphrwebapp.hr.invoice.generator.InvoiceGenerator;
import pl.uginf.rcphrwebapp.hr.invoice.generator.ServiceInfo;
import pl.uginf.rcphrwebapp.hr.invoice.model.InvoiceInfo;
import pl.uginf.rcphrwebapp.hr.invoice.model.InvoiceInfoRecord;
import pl.uginf.rcphrwebapp.hr.user.UserRepository;
import pl.uginf.rcphrwebapp.hr.user.dto.AddressDto;
import pl.uginf.rcphrwebapp.hr.user.model.Address;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.hr.user.service.UserService;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InvoiceServiceImpl implements InvoiceService {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM.yyyy");

    private final UserService userService;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final InvoiceGenerator invoiceGenerator;

    @Override
    public MultipartFile generateInvoiceForUser(String username, YearMonth monthForInvoice) {
        List<WorkInfoDto> workInfosBetween = getWorkInfosForMonth(username, monthForInvoice);

        if ( CollectionUtils.isEmpty(workInfosBetween) ) {
            throw new InvoiceGenerationException(
                    "User %s for month %s does not have any work contract with hourly rate.".formatted(username, monthForInvoice.format(dateTimeFormatter)));
        }

        List<WorkLog> workLogsForUser = getWorkLogsForMonth(username, monthForInvoice);

        InvoiceInfoRecord invoiceInfoRecord = getInvoiceInfoForUser(username);
        List<ServiceInfo> serviceInfos = createServiceInfo(workInfosBetween, workLogsForUser);

        return invoiceGenerator.generateInvoice(new InvoiceData(invoiceInfoRecord, serviceInfos), monthForInvoice);
    }

    @Override
    public InvoiceInfoRecord getInvoiceInfoForUser(String username) {
        User user = userService.getUserByUsername(username);
        InvoiceInfo invoiceInfo = user.getInvoiceInfo();
        if ( invoiceInfo == null ) {
            return null;
        }
        AddressDto addressDto = modelMapper.map(invoiceInfo.getAddress(), AddressDto.class);
        return new InvoiceInfoRecord(invoiceInfo.getCompanyName(), invoiceInfo.getNip(), addressDto, invoiceInfo.getAccountNumber());
    }

    @Override
    public InvoiceInfoRecord setInvoiceInfoForUser(String username, InvoiceInfoRecord invoiceInfoRecord) {
        User user = userService.getUserByUsername(username);
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setNip(invoiceInfoRecord.nip());
        invoiceInfo.setCompanyName(invoiceInfoRecord.companyName());
        invoiceInfo.setAccountNumber(invoiceInfoRecord.accountNumber());
        invoiceInfo.setAddress(modelMapper.map(invoiceInfoRecord.addressDto(), Address.class));
        user.setInvoiceInfo(invoiceInfo);
        userRepository.save(user);
        return invoiceInfoRecord;
    }

    private List<WorkInfoDto> getWorkInfosForMonth(String username, YearMonth month) {
        List<WorkInfoDto> workInfos = userService.getWorkInfosForUser(username);
        LocalDate fromFilter = month.atDay(1);
        LocalDate toFilter = getFirstDayOfNextMonth(month);
        return workInfos.stream()
                .filter(workInfo -> workInfo.getTo() == null || fromFilter.isBefore(workInfo.getTo()
                        .toLocalDate()))
                .filter(workInfo -> toFilter.isAfter(workInfo.getFrom()
                        .toLocalDate()))
                .filter(workInfo -> workInfo.getHourlyRate() != null)
                .sorted(Comparator.comparing(WorkInfoDto::getFrom))
                .toList();
    }

    private List<WorkLog> getWorkLogsForMonth(String username, YearMonth month) {
        return userService.getWorkLogsForUser(username, Date.valueOf(month.atDay(1)), Date.valueOf(getFirstDayOfNextMonth(month)));
    }

    private List<ServiceInfo> createServiceInfo(List<WorkInfoDto> workInfos, List<WorkLog> workLogs) {
        Map<WorkInfoDto, Double> hoursWorkedMap = new HashMap<>();
        for (WorkLog workLog : workLogs) {
            for (WorkInfoDto workInfo : workInfos) {
                double workedHours = countWorkedHours(workLog, workInfo);
                Double lastHoursWorked = hoursWorkedMap.get(workInfo);
                double hoursWorked = lastHoursWorked == null ? workedHours : workedHours + lastHoursWorked;
                hoursWorkedMap.put(workInfo, hoursWorked);
            }
        }
        return createServiceInfoFromMap(hoursWorkedMap);
    }

    private List<ServiceInfo> createServiceInfoFromMap(Map<WorkInfoDto, Double> hoursWorkedMap) {
        return hoursWorkedMap.entrySet()
                .stream()
                .map(entry -> {
                    WorkInfoDto workInfo = entry.getKey();
                    BigDecimal hourlyRate = workInfo.getHourlyRate();
                    BigDecimal allPayment = BigDecimal.valueOf(entry.getValue())
                            .multiply(hourlyRate)
                            .setScale(2, RoundingMode.HALF_UP);
                    return new ServiceInfo(workInfo.getJobRole(), allPayment, hourlyRate);
                })
                .collect(Collectors.toList());
    }

    private double countWorkedHours(WorkLog workLog, WorkInfoDto workInfoDto) {
        java.util.Date workLogFrom = workLog.getFrom();
        java.util.Date workLogTo = workLog.getTo();

        Date workInfoFrom = workInfoDto.getFrom();
        Date workInfoTo = workInfoDto.getTo();

        if ( workLogTo == null ) {
            return 0;
        }
        return getHoursBetweenForSecondDate(workInfoFrom, workInfoTo, workLogFrom, workLogTo);
    }

}
