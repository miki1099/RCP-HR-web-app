package pl.uginf.rcphrwebapp.hr.calendar.holiday.externalapimodel;

import java.util.Date;

import lombok.Data;

@Data
public class HolidayApiResponse {
    public Date date;

    public String name;
}
