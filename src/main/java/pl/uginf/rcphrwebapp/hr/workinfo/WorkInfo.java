package pl.uginf.rcphrwebapp.hr.workinfo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.uginf.rcphrwebapp.hr.user.model.User;

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
    private Date to;

    @Column(name = "CONTRACT_TYPE", nullable = false)
    private String contractType;

    @Column(name = "JOB_ROLE")
    private String jobRole;

    @Column(name = "DAILY_WORKING_TIME", nullable = false)
    private short dailyWorkingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User userId;
}
