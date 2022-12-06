package com.demo.testpractice.payment.stripe;

import com.demo.testpractice.payment.CardPaymentCharge;
import com.demo.testpractice.payment.CardPaymentCharger;
import com.demo.testpractice.payment.Currency;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "false"
)
public class MockStripeService implements CardPaymentCharger {
    @Override
    public CardPaymentCharge chargeCard(BigDecimal amount, Currency currency, String cardSource, String description) {
        return new CardPaymentCharge(true);
    }
}
