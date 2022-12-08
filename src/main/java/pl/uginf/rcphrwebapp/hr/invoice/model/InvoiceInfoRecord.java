package pl.uginf.rcphrwebapp.hr.invoice.model;

import org.apache.commons.lang3.StringUtils;

import pl.uginf.rcphrwebapp.hr.user.dto.AddressDto;

public record InvoiceInfoRecord(String companyName, String nip, AddressDto addressDto, String accountNumber) {

    public String toString(boolean withAccountNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        String newLine = System.lineSeparator();
        stringBuilder.append(companyName)
                .append(newLine)
                .append(addressDto.getStreet())
                .append(StringUtils.SPACE)
                .append(addressDto.getHomeNumber())
                .append(newLine)
                .append(addressDto.getPostalCode())
                .append(StringUtils.SPACE)
                .append(addressDto.getCity())
                .append(newLine)
                .append("NIP: ")
                .append(nip);

        if ( withAccountNumber ) {
            stringBuilder.append(newLine)
                    .append(newLine)
                    .append("Account number: ")
                    .append(accountNumber);
        }
        return stringBuilder.toString();
    }
}
