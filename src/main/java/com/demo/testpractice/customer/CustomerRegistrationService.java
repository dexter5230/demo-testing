package com.demo.testpractice.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

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
        // if the request is null, throw null request exception
        if (request == null) {
            throw new IllegalStateException("Null Request");
        }
        // Get the phone number from the request
        String phoneNumber = request.getCustomer().getPhoneNumber();

        // if the number is null or length is zero, throw new IllegalStateException
        if (phoneNumber == null || phoneNumber.length() == 0) {
            throw new IllegalStateException("The phone number cannot be empty");
        }

        //Try to find whether the phone belongs to one of the customers
        Optional<Customer> customerOptional = customerRepository.selectCustomerByPhoneNumber(phoneNumber);
        // if it is already in the system, throw an exception
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            if (customer.getName().equals(request.getCustomer().getName())) {
                throw new IllegalStateException(String.format("The customer with phone number [%s] has been registered already", phoneNumber));
            } else {
                throw new IllegalStateException(String.format("The phone number [%s] has been taken by other customer already", phoneNumber));
            }
        }

        // if the id of customer is null, generate the id for customer
        if (request.getCustomer().getId() == null) {
            request.getCustomer().setId(UUID.randomUUID());
        }

        //Save the customer to database
        customerRepository.save(request.getCustomer());
    }
}