package pl.uginf.rcphrwebapp.hr.user.dto;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uginf.rcphrwebapp.hr.user.model.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    private Set<Role> roles;

    private boolean isActive;
}
