package com.demo.testpractice.payment;

import com.demo.testpractice.customer.Customer;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "Payment")
@Table(name = "payment")

public class Payment {
    @Id
    @SequenceGenerator(name = "payment_id_generator", sequenceName = "payment_id_generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column
    private BigDecimal amount;

    @Column
    private Currency currency;

    @Column
    private String source;

    @Column
    private String description;

    @ManyToOne(cascade = {CascadeType.DETACH} , fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", foreignKey = @ForeignKey(name = "customer_id_fk"))
    private Customer customer;


    public Payment(Long paymentId, Customer customer, BigDecimal amount, Currency currency, String source, String description) {
        this.paymentId = paymentId;
        this.customer = customer;
        this.amount = amount;
        this.currency = currency;
        this.source = source;
        this.description = description;
    }

    public Payment() {
    }



    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }



    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

