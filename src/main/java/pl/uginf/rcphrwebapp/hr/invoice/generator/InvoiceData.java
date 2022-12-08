package pl.uginf.rcphrwebapp.hr.invoice.generator;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.uginf.rcphrwebapp.hr.invoice.model.InvoiceInfoRecord;

@Data
@AllArgsConstructor
public class InvoiceData {

    private InvoiceInfoRecord invoiceInfoRecord;

    private List<ServiceInfo> serviceInfoList;
}
