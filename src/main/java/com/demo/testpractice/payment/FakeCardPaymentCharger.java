package com.demo.testpractice.payment;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class FakeCardPaymentCharger implements CardPaymentCharger {
    @Override
    public CardPaymentCharge chargeCard(BigDecimal amount, Currency currency, String cardSource, String description) {
        return new CardPaymentCharge(true);
    }
}
