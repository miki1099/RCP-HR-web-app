package pl.uginf.rcphrwebapp.hr.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserDto {
    private String pesel;

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Date hireDate;

    private Date birthDate;

    private AddressDto address;

    private boolean isActive;
}
