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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserValidator {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    @SuppressWarnings("FieldCanBeLocal")
    private final String FIRSTNAME_REGEX = "[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]+";
    @SuppressWarnings("FieldCanBeLocal")
    private final String LASTNAME_REGEX = "[A-ZŻŹĆĄŚĘŁÓŃ]'?[A-Z]?[a-zżźćńółęąś]*-?[A-ZŻŹĆĄŚĘŁÓŃ]?'?[A-Z]?[a-zżźćńółęąś]*";

    public void validate(UserDto userDto) throws ValidationException, ParseException {
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

    private void validateBirthDate(Date date, String pesel, List<String> errors) throws ParseException {
        Date peselDate = getDateFromPesel(pesel);
        if (date.compareTo(peselDate) != 0) {
            String formattedDate = new SimpleDateFormat(DATE_PATTERN).format(date.getTime());
            errors.add(MsgCodes.BIRTH_DATE_NOT_MATCH_PESEL.getMsg(formattedDate));
        }
    }

    private Date getDateFromPesel(String pesel) throws ParseException {
        String day = pesel.substring(4, 6);
        String month = getMonth(pesel.substring(2, 4));
        String year = getYear(pesel.substring(0, 4));
        String stringDate = year + "-" + month + "-" + day;
        return new SimpleDateFormat(DATE_PATTERN).parse(stringDate);
    }

    private String getYear(String peselYearAndMonth) {
        StringBuilder twoNumberYear = new StringBuilder(peselYearAndMonth.substring(0, 2));
        StringBuilder peselMonth = new StringBuilder(peselYearAndMonth.substring(2, 4));

        if (peselMonth.charAt(0) == '0' || (peselMonth.charAt(0) == '1')) {
            return twoNumberYear.insert(0, "19").toString();
        } else if (peselMonth.charAt(0) == '2' || (peselMonth.charAt(0) == '3')) {
            return twoNumberYear.insert(0, "20").toString();
        } else
            return twoNumberYear.insert(0, "18").toString();
    }

    private String getMonth(String peselMonth) {
        if (peselMonth.charAt(0) == '0' || (peselMonth.charAt(0) == '1')) {
            return peselMonth;
        } else if (peselMonth.charAt(0) == '2' || (peselMonth.charAt(0) == '8')) {
            return "0" + peselMonth.charAt(1);
        } else {
            return "1" + peselMonth.charAt(1);
        }
    }

}
//TODO use return or revert if
//TODO simplify date validate?
