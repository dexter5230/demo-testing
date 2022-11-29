package com.demo.testpractice.payment;

import com.demo.testpractice.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;

    private final List<Currency> SUPPORTED_CURRENCY = List.of(Currency.AUD, Currency.RMB);

    @Autowired
    public PaymentService(CustomerRepository customerRepository, PaymentRepository paymentRepository, FakeCardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    public void chargeCard(PaymentRequest paymentRequest) {
        if (paymentRequest.getPayment().getCustomer() == null) {
            throw new IllegalStateException(String.format("The request of customer with payment id [%s] is null", paymentRequest.getPayment().getPaymentId()));
        }
        UUID customerId = paymentRequest.getPayment().getCustomer().getId();
        if (customerRepository.findById(customerId).isEmpty()){
            throw new IllegalStateException("The customer with Id " + customerId + " does not exist in the database");
        }
        boolean supCurrency = false;
        for (Currency currency : this.SUPPORTED_CURRENCY) {
            if (currency.equals(paymentRequest.getPayment().getCurrency())) {
                supCurrency = true;
                break;
            }
        }
        if (!supCurrency) {
            throw new IllegalStateException("The currency " + paymentRequest.getPayment().getCurrency() + " is not supported");
        }

        //charge card
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(paymentRequest.getPayment().getAmount(),
                                      paymentRequest.getPayment().getCurrency(),
                                      paymentRequest.getPayment().getSource(),
                                      paymentRequest.getPayment().getDescription());

        //check debited or not
        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException("The debited is false");
        }

        //Inset payment
        paymentRepository.save(paymentRequest.getPayment());

        // TO-DO: Send the request
    }
}
