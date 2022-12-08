package pl.uginf.rcphrwebapp.hr.invoice.counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class InvoiceCounterService {

    private final InvoiceCounterRepository invoiceCounterRepository;

    public Long getCounter() {
        InvoiceCounter counter = invoiceCounterRepository.getCounter();
        if ( counter == null ) {
            counter = new InvoiceCounter();
            counter.setCounter(0L);
        }
        Long invoiceCounter = counter.getCounter();
        counter.setCounter(++invoiceCounter);

        invoiceCounterRepository.save(counter);
        return invoiceCounter;
    }
}
