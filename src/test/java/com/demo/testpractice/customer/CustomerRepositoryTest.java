package com.demo.testpractice.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {
    private final CustomerRepository underTest;

    @Autowired
    public CustomerRepositoryTest(CustomerRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        //Given
        Customer customer = new Customer(UUID.randomUUID(), "Jiaxuan","+61403562186");

        //When
        underTest.save(customer);

        //Then
        Optional<Customer> res = underTest.selectCustomerByPhoneNumber(customer.getPhoneNumber());
        assertThat(res).isPresent().hasValueSatisfying(r->{
            assertThat(r).usingRecursiveComparison().isEqualTo(customer);
        });
    }

    @Test
    void itShouldSaveCustomer() {
        //Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Jiaxuan","+61403562186");

        //When
        underTest.save(customer);

        //Then
        Optional<Customer> optionalCustomer = underTest.findById(id);
        assertThat(optionalCustomer).hasValueSatisfying(c -> {
            assertThat(c).usingRecursiveComparison()
                    .isEqualTo(customer);
//            assertThat(c.getId()).isEqualTo(id);
//            assertThat(c.getName()).isEqualTo(customer.getName());
//            assertThat(c.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        });
    }
}