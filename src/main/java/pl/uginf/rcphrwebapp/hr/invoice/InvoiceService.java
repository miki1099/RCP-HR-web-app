package pl.uginf.rcphrwebapp.hr.invoice;

import java.time.YearMonth;

import org.springframework.core.io.Resource;

import pl.uginf.rcphrwebapp.hr.invoice.model.InvoiceInfoRecord;

public interface InvoiceService {

    Resource generateInvoiceForUser(String username, YearMonth monthForInvoice);

    InvoiceInfoRecord getInvoiceInfoForUser(String username);

    InvoiceInfoRecord setInvoiceInfoForUser(String username, InvoiceInfoRecord invoiceInfoRecord);
}
