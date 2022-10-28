package pl.uginf.rcphrwebapp.hr.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.uginf.rcphrwebapp.hr.ValidationException;
import pl.uginf.rcphrwebapp.hr.user.dto.AddressDto;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class UserValidatorTest {

    private final UserValidator userValidator = new UserValidator();

    @Test
    public void validateValidUserTest() {
        //given
        UserDto userDto = createUser(true);

        //when
        userValidator.validate(userDto);

        //then
        // no validation exception test passes
    }

    @Test
    public void validateInvalidUserTest() {
        //given
        UserDto userDto = createUser(false);

        //when
        ValidationException error = Assertions.assertThrows(ValidationException.class, () -> userValidator.validate(userDto));

        //then
        assertThat(error.getMessage()).isEqualTo("""
                Invalid first name
                Invalid last name
                Invalid email
                City can not be empty
                Street can not be empty
                Country can not be empty
                Postal code can not be empty
                Invalid pesel
                Password is too short""");
    }

    private UserDto createUser(boolean valid) {
        String pesel = "53032231865";
        String password = "Password1234";
        String email = "email@ex.com";
        String firstName = "Michał";
        String lastName = "Polśążźćł";
        String street = "Street19";
        String city = "Gdańsk";
        String country = "Polska";
        String postalCode = "80-818";
        if (!valid) {
            pesel = "99999999999";
            password = "xxx";
            email = "email";
            firstName = "f";
            lastName = "y";
            street = "";
            city = null;
            country = "";
            postalCode = "";

        }
        return UserDto.builder()
                .pesel(pesel)
                .username("user.name")
                .password(password)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phone("606389123")
                .hireDate(new Date())
                .birthdayDate(new Date())
                .isActive(true)
                .address(new AddressDto(street, null, city, country, postalCode))
                .build();
    }
}