package pl.uginf.rcphrwebapp.hr.workinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkInfoDto {

    private Date from;

    private Date TO;

    private String contractType;

    private String jobRole;

    private short dailyWorkingTime;

    private String username;
}
