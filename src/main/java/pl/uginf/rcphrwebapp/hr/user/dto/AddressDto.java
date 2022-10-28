package pl.uginf.rcphrwebapp.hr.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressDto {
    private String street;

    private String homeNumber;

    private String city;

    private String country;

    private String postalCode;
}
