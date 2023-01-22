package pl.uginf.rcphrwebapp.hr.benefits;

import java.math.BigDecimal;

public record BenefitRecord(Long id, String details, BigDecimal monthlyCost) {
}
