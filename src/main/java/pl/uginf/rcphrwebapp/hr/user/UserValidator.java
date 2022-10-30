package pl.uginf.rcphrwebapp.hr.user;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.pl.PESELValidator;
import org.springframework.stereotype.Component;
import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.hr.user.dto.AddressDto;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.utils.MsgCodes;
import pl.uginf.rcphrwebapp.validator.Validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserValidator implements Validator<UserDto> {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String PESEL_DATE_PATTERN = "yyyyMMdd";

    private static final String FIRSTNAME_REGEX = "[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]+";

    private static final String LASTNAME_REGEX = "[A-ZŻŹĆĄŚĘŁÓŃ]'?[A-Z]?[a-zżźćńółęąś]*-?[A-ZŻŹĆĄŚĘŁÓŃ]?'?[A-Z]?[a-zżźćńółęąś]*";

    @Override
    public void validate(UserDto userDto) throws ValidationException {
        List<String> errors = new ArrayList<>();
        firstNameCheck(userDto.getFirstName(), errors);
        lastNameCheck(userDto.getLastName(), errors);
        emailCheck(userDto.getEmail(), errors);
        validateAddress(userDto.getAddress(), errors);
        peselValidate(userDto.getPesel(), errors);
        validatePassword(userDto.getPassword(), errors);
        validateBirthDate(userDto.getBirthDate(), userDto.getPesel(), errors); //must be after peselValidate
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

    private void validateBirthDate(Date birthDate, String pesel, List<String> errors) {
        String peselBirthPart = pesel.substring(0, 6);
        String peselBirthDate = getStringDateFromPesel(peselBirthPart);
        String birthDateString = getDateString(birthDate);


        if (!peselBirthDate.equals(birthDateString)) {
            String formattedDate = new SimpleDateFormat(DATE_PATTERN).format(birthDate.getTime());
            errors.add(MsgCodes.BIRTH_DATE_NOT_MATCH_PESEL.getMsg(formattedDate, peselBirthPart));
        }
    }

    private String getDateString(Date birthDate) {
        return new SimpleDateFormat(PESEL_DATE_PATTERN).format(birthDate.getTime());
    }

    private String getStringDateFromPesel(String pesel) {
        char monthFirstNumber = pesel.charAt(2);
        int yearStart = 19;
        char monthFirstNumberToReplace = 0;
        switch (monthFirstNumber) {
            case '2', '3' -> yearStart = 20;
            case '4', '5' -> yearStart = 21;
            case '6', '7' -> yearStart = 22;
        }
        switch (monthFirstNumber) {
            case '2', '4', '6' -> monthFirstNumberToReplace = '0';
            case '3', '5', '7' -> monthFirstNumberToReplace = '1';
        }

        StringBuilder peselBuilder = new StringBuilder(pesel);
        if (yearStart != 19) {
            peselBuilder.setCharAt(2, monthFirstNumberToReplace);
        }
        peselBuilder.insert(0, yearStart);
        return peselBuilder.toString();
    }
}
