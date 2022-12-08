package pl.uginf.rcphrwebapp.hr.invoice.counter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceCounterRepository extends JpaRepository<InvoiceCounter, Long> {

    @Query("SELECT c FROM InvoiceCounter c WHERE c.id = -1")
    InvoiceCounter getCounter();

}
