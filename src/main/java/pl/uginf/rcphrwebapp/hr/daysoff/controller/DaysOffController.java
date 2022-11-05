package pl.uginf.rcphrwebapp.hr.daysoff.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.uginf.rcphrwebapp.hr.daysoff.model.DayOffType;

@RestController
@RequestMapping("/daysOff")
public class DaysOffController {

    @GetMapping("/Types")
    public List<DayOffType> getAllPossibleTypes() {
        return List.of(DayOffType.values());
    }
}
