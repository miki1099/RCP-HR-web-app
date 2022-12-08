package pl.uginf.rcphrwebapp.hr.invoice.model;

import pl.uginf.rcphrwebapp.hr.user.dto.AddressDto;

public record InvoiceInfoRecord(String companyName, String nip, AddressDto addressDto, String accountNumber) {
}
