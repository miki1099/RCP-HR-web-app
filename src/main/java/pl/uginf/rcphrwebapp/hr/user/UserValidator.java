package pl.uginf.rcphrwebapp.hr.user;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.pl.PESELValidator;
import org.springframework.stereotype.Component;
import pl.uginf.rcphrwebapp.hr.ValidationException;
import pl.uginf.rcphrwebapp.hr.user.dto.AddressDto;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.utils.MsgCodes;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidator {
    private final String FIRSTNAME_REGEX = "[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]+";

    private final String LASTNAME_REGEX = "[A-ZŻŹĆĄŚĘŁÓŃ]'?[A-Z]?[a-zżźćńółęąś]*-?[A-ZŻŹĆĄŚĘŁÓŃ]?'?[A-Z]?[a-zżźćńółęąś]*";

    public void validate(UserDto userDto) throws ValidationException {
        List<String> errors = new ArrayList<>();
        firstNameCheck(userDto.getFirstName(), errors);
        lastNameCheck(userDto.getLastName(), errors);
        emailCheck(userDto.getEmail(), errors);
        validateAddress(userDto.getAddress(), errors);
        peselValidate(userDto.getPesel(), errors);
        validatePassword(userDto.getPassword(), errors);
        //TODO validate birth date with pesel + test
        if (CollectionUtils.isNotEmpty(errors)) {
            throw new ValidationException(errors);
        }
    }

    private void firstNameCheck(String firstName, List<String> errors) {
        if (firstName != null && firstName.matches(FIRSTNAME_REGEX)) {
            return;
        }
        errors.add(MsgCodes.INVALID.getMsg("first name"));
    }

    private void lastNameCheck(String lastName, List<String> errors) {
        if (lastName != null && lastName.matches(LASTNAME_REGEX)) {
            return;
        }
        errors.add(MsgCodes.INVALID.getMsg("last name"));
    }

    private void emailCheck(String email, List<String> errors) {
        EmailValidator emailValidator = new EmailValidator();
        if (StringUtils.isNotEmpty(email) && emailValidator.isValid(email, null)) {
            return;
        }
        errors.add(MsgCodes.INVALID.getMsg("email"));
    }

    private void validateAddress(AddressDto address, List<String> errors) {
        if (StringUtils.isEmpty(address.getCity())) {
            errors.add(MsgCodes.ERROR_EMPTY.getMsg("City"));
        }
        if (StringUtils.isEmpty(address.getStreet())) {
            errors.add(MsgCodes.ERROR_EMPTY.getMsg("Street"));
        }
        if (StringUtils.isEmpty(address.getCountry())) {
            errors.add(MsgCodes.ERROR_EMPTY.getMsg("Country"));
        }
        if (StringUtils.isEmpty(address.getPostalCode())) {
            errors.add(MsgCodes.ERROR_EMPTY.getMsg("Postal code"));
        }
    }

    private void peselValidate(String pesel, List<String> errors) {
        PESELValidator peselValidator = new PESELValidator();
        peselValidator.initialize(null);
        if (!peselValidator.isValid(pesel, null)) {
            errors.add(MsgCodes.INVALID.getMsg("pesel"));
        }
    }

    private void validatePassword(String password, List<String> errors) {
        if (StringUtils.isEmpty(password)) {
            errors.add(MsgCodes.INVALID.getMsg(password));
        }
        if (password.length() < 8) {
            errors.add("Password is too short");
        }
    }

}
