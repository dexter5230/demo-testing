package com.demo.testpractice.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    @Query("SELECT c FROM Customer c WHERE c.phoneNumber = :phoneNumber")
    Optional<Customer> selectCustomerByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
