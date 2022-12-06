package com.demo.testpractice.admin;

import com.demo.testpractice.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerManagementService {
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerManagementService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Modifying
    public void removeCustomer(CustomerRemoveRequest customerRemoveRequest) {
        if (customerRemoveRequest.getCustomer() == null) {
            throw new IllegalStateException("The customer is null");
        }
        boolean isExist = customerRepository.findById(customerRemoveRequest.getCustomer().getId()).isPresent();
        if (!isExist) {
            throw new IllegalStateException("The customer is not exist");
        } else {
            customerRepository.deleteById(customerRemoveRequest.getCustomer().getId());
        }
    }
}
