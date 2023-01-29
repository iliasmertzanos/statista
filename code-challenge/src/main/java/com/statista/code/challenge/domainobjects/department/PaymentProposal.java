package com.statista.code.challenge.domainobjects.department;

import java.math.BigDecimal;

public record PaymentProposal(
        BigDecimal paymentRateAmount,
        BigDecimal upfrontFee,
        BigDecimal ratesTotal
) {
}