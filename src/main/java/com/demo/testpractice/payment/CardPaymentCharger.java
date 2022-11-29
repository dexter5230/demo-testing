package com.demo.testpractice.payment;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public interface CardPaymentCharger {
    CardPaymentCharge chargeCard(
            BigDecimal amount,
            Currency currency,
            String cardSource,
            String description
    );
}
