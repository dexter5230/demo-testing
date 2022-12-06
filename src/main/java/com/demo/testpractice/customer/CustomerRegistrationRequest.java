package com.demo.testpractice.customer;

public class CustomerRegistrationRequest {

    private Customer customer;

    public CustomerRegistrationRequest(Customer customer) {
        this.customer = customer;
    }

    public CustomerRegistrationRequest() {
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "CustomerRegistrationRequest{" +
                "customer=" + customer +
                '}';
    }
}
