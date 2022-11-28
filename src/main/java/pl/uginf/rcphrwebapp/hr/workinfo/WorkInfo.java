package pl.uginf.rcphrwebapp.hr.workinfo;

import java.math.BigDecimal;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    @Temporal(TemporalType.DATE)
    private Date from;

    @Column(name = "INFO_END")
    @Temporal(TemporalType.DATE)
    private Date to;

    @Column(name = "CONTRACT_TYPE", nullable = false)
    private String contractType;

    @Column(name = "JOB_ROLE")
    private String jobRole;

    @Column(name = "HOURLY_RATE")
    private BigDecimal hourlyRate;

    @Column(name = "DAILY_WORKING_TIME", nullable = false)
    private short dailyWorkingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User userId;
}
