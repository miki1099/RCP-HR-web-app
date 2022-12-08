package pl.uginf.rcphrwebapp.hr.invoice.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.uginf.rcphrwebapp.hr.user.model.Address;

@Entity
@Table(name = "INVOICE_INFO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS", referencedColumnName = "ID")
    private Address address;

    @Column(name = "NIP")
    private String nip;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

}
