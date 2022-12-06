package com.demo.testpractice.payment;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public interface CardPaymentCharger {
    CardPaymentCharge chargeCard(
            BigDecimal amount,
            Currency currency,
            String cardSource,
            String description
    );
}
