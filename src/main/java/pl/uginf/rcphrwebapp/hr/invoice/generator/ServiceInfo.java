package pl.uginf.rcphrwebapp.hr.invoice.generator;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceInfo {

    private String name;

    private BigDecimal quantity;

    private BigDecimal hourRate;
}
