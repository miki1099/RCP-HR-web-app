package pl.uginf.rcphrwebapp.hr.calendar.holiday;

import static pl.uginf.rcphrwebapp.hr.calendar.DateHelper.getLocalDate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.uginf.rcphrwebapp.hr.calendar.dto.CalendarEventRecord;
import pl.uginf.rcphrwebapp.hr.calendar.holiday.externalapimodel.HolidayApiResponse;

@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class CountryHolidayService {

    public Set<CalendarEventRecord> getHolidaysForCountry(String country) {
        int currentYear = LocalDate.now()
                .get(ChronoField.YEAR);
        HolidayApiResponse[] response;
        HolidayApiResponse[] responseNextYear;
        try {
            response = getHolidayApiResponse(country, currentYear);
            responseNextYear = getHolidayApiResponse(country, currentYear + 1);

        } catch (IOException | InterruptedException e) {
            log.error("Country holiday api is not available for country {}", country);
            return new HashSet<>();
        }
        Set<CalendarEventRecord> holidays = new HashSet<>(mapOlderThanNowHolidaysResponseToEvents(response));
        holidays.addAll(mapOlderThanNowHolidaysResponseToEvents(responseNextYear));
        return holidays;
    }

    private Set<CalendarEventRecord> mapOlderThanNowHolidaysResponseToEvents(HolidayApiResponse[] response) {
        List<HolidayApiResponse> holidays = Arrays.asList(response);
        Date now = new Date();
        return holidays.stream()
                .filter(holiday -> now.before(holiday.getDate()))
                .map(holiday -> new CalendarEventRecord(holiday.getName(), getLocalDate(holiday.getDate())))
                .collect(Collectors.toSet());
    }

    private HolidayApiResponse[] getHolidayApiResponse(String country, int year) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://public-holiday.p.rapidapi.com/" + year + "/" + country))
                .header("X-RapidAPI-Key", "d209bceccemsh82eef29d36c9f0cp18a78bjsn9f79965071ac")
                .header("X-RapidAPI-Host", "public-holiday.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), HolidayApiResponse[].class);
    }

}
