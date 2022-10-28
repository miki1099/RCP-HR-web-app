package pl.uginf.rcphrwebapp.hr.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "STREET", nullable = false)
    private String street;

    @Column(name = "HOME_NUMBER")
    private String homeNumber;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Column(name = "COUNTRY", nullable = false)
    private String country;

    @Column(name = "POSTAL_CODE", nullable = false)
    private String postalCode;
}
