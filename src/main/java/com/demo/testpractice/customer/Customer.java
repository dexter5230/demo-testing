package com.demo.testpractice.customer;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.UUID;


@Entity(name = "Customer")
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(name = "unique_phone_number_constrains", columnNames = "phone_number"))

public class Customer {

    @Id
    private UUID id;

    @NotBlank
    @Column(name = "customer_name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @NotBlank
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    public Customer(UUID id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Customer(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Customer() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}