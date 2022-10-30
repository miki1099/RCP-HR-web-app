package pl.uginf.rcphrwebapp.hr.workinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.uginf.rcphrwebapp.hr.user.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WORK_INFO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "INFO_START", nullable = false)
    private Date from;

    @Column(name = "INFO_END")
    private Date TO;

    @Column(name = "CONTRACT_TYPE", nullable = false)
    private String contractType;

    @Column(name = "JOB_ROLE")
    private String jobRole;

    @Column(name = "DAILY_WORKING_TIME", nullable = false)
    private short dailyWorkingTime;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User userId;
}
