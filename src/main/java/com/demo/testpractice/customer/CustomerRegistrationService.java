package com.demo.testpractice.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Modifying
    public void registerNewCustomer(CustomerRegistrationRequest request) {
        Optional<Customer> customer = customerRepository.selectCustomerByPhoneNumber(request.getCustomer().getPhoneNumber());
        if(customer.isPresent()) {throw new IllegalStateException("customer is already exist");} else {
            customerRepository.save(request.getCustomer());
        }
    }
}