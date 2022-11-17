package pl.uginf.rcphrwebapp.hr.calendar.holiday.externamapimodel;

import java.util.Date;

import lombok.Data;

@Data
public class HolidayApiResponse {
    public Date date;

    public String name;
}
