package pl.uginf.rcphrwebapp.hr.user.summary.dto;

import pl.uginf.rcphrwebapp.hr.benefits.BenefitRecord;

import java.math.BigDecimal;
import java.util.List;

public record TheoreticalSummaryRecord(BigDecimal shouldEarnGross, int shouldWorkMinutes, List<BenefitRecord> benefits) {
}
