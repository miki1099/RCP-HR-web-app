package pl.uginf.rcphrwebapp.rcp.worklog.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uginf.rcphrwebapp.hr.user.model.User;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLogFlag;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkLogDTO {

    private Date from;

    private Date to;

    private WorkLogFlag status;

    private String comment;

    private boolean isApproved;

    private User user;
}
