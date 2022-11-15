package pl.uginf.rcphrwebapp.hr.user.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.uginf.rcphrwebapp.hr.daysoff.model.DaysOff;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfo;
import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

@Entity
@Table(name = "PERSONS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @Column(name = "PESEL", nullable = false, unique = true)
    private String pesel;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "HIRE_DATE")
    private Date hireDate;

    @Column(name = "BIRTH_DATE")
    private Date birthDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS", referencedColumnName = "ID")
    private Address address;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "BOSS", referencedColumnName = "username")
    private User boss; //TODO reportTo or Boss

    @OneToMany(mappedBy = "userId")
    private List<WorkInfo> workInfos;

    @OneToMany(mappedBy = "user")
    private List<WorkLog> workLogs;

    @OneToMany(mappedBy = "user")
    private List<DaysOff> daysOffList;
}
