package pl.uginf.rcphrwebapp.hr.user.summary.dto;

import pl.uginf.rcphrwebapp.hr.invoice.model.InvoiceInfoRecord;

public record CostSummaryRecord(int workedMinutes, TheoreticalSummaryRecord theoreticalSummaryRecord, InvoiceInfoRecord invoiceInfoRecord) {
}
