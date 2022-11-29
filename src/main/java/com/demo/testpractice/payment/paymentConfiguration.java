package com.demo.testpractice.payment;

import com.demo.testpractice.customer.Customer;
import com.demo.testpractice.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.UUID;

public class paymentConfiguration {
    private final PaymentService paymentService;
    private final CustomerRepository customerRepository;

    @Autowired
    public paymentConfiguration(PaymentService paymentService, CustomerRepository customerRepository) {
        this.paymentService = paymentService;
        this.customerRepository = customerRepository;
    }


}