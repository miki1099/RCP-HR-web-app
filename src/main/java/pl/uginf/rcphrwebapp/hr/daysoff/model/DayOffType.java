package pl.uginf.rcphrwebapp.hr.daysoff.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DayOffType {
    PAID_HOLIDAY("Paid holiday"),
    UNPAID_HOLIDAY("Unpaid holiday"),
    SICK_LEAVE("Sick leave"),
    PARENTAL_LEAVE("Parental leave"),
    DELEGATION("Delegation"),
    FUNEREAL("Funeral");

    private final String name;
}
