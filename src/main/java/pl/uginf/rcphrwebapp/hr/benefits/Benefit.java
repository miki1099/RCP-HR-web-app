package pl.uginf.rcphrwebapp.hr.benefits;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "BENEFITS")
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "DETAILS", nullable = false)
    private String details;

    @Column(name = "MONTHLY_COST", nullable = false)
    private BigDecimal monthlyCost;
}
