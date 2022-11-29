package com.demo.testpractice.payment;

import com.demo.testpractice.customer.Customer;
import com.demo.testpractice.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static com.demo.testpractice.payment.Currency.AUD;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(
        properties = {"spring.jpa.properties.javax.persistence.validation.mode=none"}
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository underTest;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void itShouldSavePayment() {
        //Given
        Customer customer = new Customer(UUID.randomUUID(), "Jiaxuan", "000", null);
        customerRepository.save(customer);
        Payment payment = new Payment(1L, customer, new BigDecimal("10.00"), AUD, "Card123","Donation");

        //When
        underTest.save(payment);
        //Then
       assertThat(underTest.findById(1L)).isPresent().hasValueSatisfying(p -> {
           assertThat(p.getCustomer().getId()).isEqualTo(customer.getId());
       });
    }
}