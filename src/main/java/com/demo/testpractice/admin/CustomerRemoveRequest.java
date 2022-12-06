package com.demo.testpractice.admin;

import com.demo.testpractice.customer.Customer;

public class CustomerRemoveRequest {
    private Customer customer;

    public CustomerRemoveRequest(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

}
