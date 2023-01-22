package pl.uginf.rcphrwebapp.hr.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uginf.rcphrwebapp.hr.user.model.Role;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private AddressDto address;

    private Set<Role> roles;
}
