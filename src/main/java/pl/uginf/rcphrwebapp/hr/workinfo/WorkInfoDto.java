package pl.uginf.rcphrwebapp.hr.workinfo;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkInfoDto {

    private Date from;

    private Date to;

    private String contractType;

    private String jobRole;

    private BigDecimal hourlyRate;

    private short dailyWorkingTime;

    private String username;
}
