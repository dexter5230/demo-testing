package com.demo.testpractice.customer;

import com.demo.testpractice.payment.Payment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Entity(name = "Customer")
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(name = "unique_phone_number_constrains", columnNames = "phone_number"))
@JsonIgnoreProperties(value = "{id}", allowGetters = true)
public class Customer {

    @Id
    @Column(name = "customer_id")
    private UUID id;

    @NotBlank
    @Column(name = "customer_name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @NotBlank
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany (mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Payment> payments;

    public Customer(UUID id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        payments = null;
    }

    public Customer(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        payments = null;
    }

    public Customer(UUID id, String name, String phoneNumber, List<Payment> payments) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.payments = payments;
    }

    public Customer() {
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
