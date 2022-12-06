package com.demo.testpractice.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/customer-registration")
public class CustomerRegistrationController {
    private final CustomerRegistrationService service;
    @Autowired
    public CustomerRegistrationController(CustomerRegistrationService service) {
        this.service = service;
    }



    @PostMapping
    public void registerNewCustomer( @RequestBody CustomerRegistrationRequest CustomerRegistrationRequest) {
        service.registerNewCustomer(CustomerRegistrationRequest);
    }
}
