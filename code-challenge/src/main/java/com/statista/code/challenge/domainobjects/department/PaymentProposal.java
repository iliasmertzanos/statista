package com.statista.code.challenge.domainobjects.department;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentProposal {
    BigDecimal paymentRateAmount;
    BigDecimal upfrontFee;
    BigDecimal ratesTotal;
}
