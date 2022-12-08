package pl.uginf.rcphrwebapp.hr.invoice.counter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "INVOICE_COUNTER")
public class InvoiceCounter {
    @Id
    private Integer id = -1;

    @Getter
    @Setter
    private Long counter;
}
