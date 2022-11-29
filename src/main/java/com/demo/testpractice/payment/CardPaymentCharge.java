package com.demo.testpractice.payment;

public class CardPaymentCharge {
    private final boolean isCarDebited;

    public CardPaymentCharge(boolean isCarDebited) {
        this.isCarDebited = isCarDebited;
    }
    public boolean isCardDebited() {
        return isCarDebited;
    }

    @Override
    public String toString() {
        return "CardPaymentCharge{" +
                "isCarDebited=" + isCarDebited +
                '}';
    }
}
