package com.demo.testpractice.customer;

public class CustomerRegistrationRequest {

    private final Customer customer;

    public CustomerRegistrationRequest(Customer customer) {
        this.customer = customer;
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
