package com.demo.testpractice.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest2 {
    @Mock
    private CustomerRepository customerRepository;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;


    private CustomerRegistrationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveCustomer() {
        //Given
        UUID id =  UUID.randomUUID();
        String name = "Jiaxuan";
        String phoneNumber = "0000";
        Customer customer = new Customer (id, name, phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //When
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.empty());
        underTest.registerNewCustomer(request);

        //Then
       then(customerRepository).should().save(customerArgumentCaptor.capture());
       Customer capturedCustomer = customerArgumentCaptor.getValue();
       assertThat(capturedCustomer).usingRecursiveComparison().isEqualTo(customer);
    }

    @Test
    void itShouldNotSaveCustomerIfNumberTaken() {
        //Given
        UUID id = UUID.randomUUID();
        String name = "Jiaxuan";
        String phoneNumber = "1234";
        Customer customer = new Customer(id, name, phoneNumber);
        Customer existingCustomer = new Customer(UUID.randomUUID(), "Siying", "1234");
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //When
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.of(existingCustomer));

        //Then
        assertThatThrownBy(()-> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("The phone number [%s] has been taken by other customer already", phoneNumber));

        then(customerRepository).should(never()).save(any(Customer.class));
    }

    @Test
    void itShouldNotSaveCustomerIfAlreadyExists() {
        //Given
        Customer customer = new Customer(UUID.randomUUID(), "Jiaxuan", "000");
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //When
        given(customerRepository.selectCustomerByPhoneNumber(customer.getPhoneNumber())).willReturn(Optional.of(customer));
        assertThatThrownBy(()->underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("The customer with phone number [%s] has been registered already", customer.getPhoneNumber()));
        //Then
        then(customerRepository).should(never()).save(any(Customer.class));
    }

    @Test
    void itShouldNotSaveCustomerIfThePhoneNumberIsNull() {
        //Given
        //When
        //Then
    }
}