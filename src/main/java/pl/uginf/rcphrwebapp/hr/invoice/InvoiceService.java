package pl.uginf.rcphrwebapp.hr.invoice;

import java.time.YearMonth;

import org.springframework.web.multipart.MultipartFile;

import pl.uginf.rcphrwebapp.hr.invoice.model.InvoiceInfoRecord;

public interface InvoiceService {

    MultipartFile generateInvoiceForUser(String username, YearMonth monthForInvoice);

    InvoiceInfoRecord getInvoiceInfoForUser(String username);

    InvoiceInfoRecord setInvoiceInfoForUser(String username, InvoiceInfoRecord invoiceInfoRecord);
}
