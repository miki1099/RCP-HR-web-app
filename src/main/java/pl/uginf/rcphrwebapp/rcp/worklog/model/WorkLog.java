package pl.uginf.rcphrwebapp.rcp.worklog.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.uginf.rcphrwebapp.hr.user.model.User;

@Entity
@Table(name = "WORK_LOG", uniqueConstraints = @UniqueConstraint(name = "USER", columnNames = { "WORKLOG_STATUS", "USER_ID" }))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "WORKLOG_FROM", nullable = false)
    private Date from;

    @Column(name = "WORKLOG_TO")
    private Date to;

    @Column(name = "WORKLOG_STATUS")
    @Enumerated(EnumType.STRING)
    private WorkLogFlag status;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "APPROVED", nullable = false)
    private boolean isApproved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}
