package pl.uginf.rcphrwebapp.hr.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String street;

    private String homeNumber;

    private String city;

    private String country;

    private String postalCode;
}
