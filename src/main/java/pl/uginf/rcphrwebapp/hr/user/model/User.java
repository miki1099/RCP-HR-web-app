package pl.uginf.rcphrwebapp.hr.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfo;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "userId")
    private List<WorkInfo> workInfos;
}
