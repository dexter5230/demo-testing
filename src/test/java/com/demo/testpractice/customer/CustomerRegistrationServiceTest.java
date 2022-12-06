package com.demo.testpractice.customer;

import com.demo.testpractice.utiles.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PhoneNumberValidator phoneNumberValidator;
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    private CustomerRegistrationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new CustomerRegistrationService(customerRepository, phoneNumberValidator);
    }

    @Test
    void itShouldSaveNewCustomer() {
        //Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Linda",phoneNumber);
        //.... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //... Not customer with the number
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.empty());
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);

        //When
        underTest.registerNewCustomer(request);

        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).usingRecursiveComparison().isEqualTo(customer);
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        //Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Linda",phoneNumber);

        //.... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);

        //... find customer with the number
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.of(customer));

        //When
        assertThatThrownBy(() ->underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("The customer with phone number [%s] has been registered already", phoneNumber));

        //Then
        then(customerRepository).should(never()).save(any(Customer.class));
    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsOthers() {
        //Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Linda",phoneNumber);
        Customer customerTwo = new Customer(UUID.randomUUID(), "Janko's",phoneNumber);



        //.... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //... find customer with the number
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.of(customerTwo));

        //When
        assertThatThrownBy(()-> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("The phone number [%s] has been taken by other customer already", phoneNumber));
        //Then
        then(customerRepository).should(never()).save(any(Customer.class));
    }

    @Test
    void itShouldNotSaveCustomerWhenRequestIsNull() {
        //....Given a request
        CustomerRegistrationRequest request = null;

        //... When Throw an IllegalException
        assertThatThrownBy(()-> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Null Request");

        //Then
        then(customerRepository).should(never()).save(any(Customer.class));
    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsIllegal() {
        //Given a phone number and a customer
        String phoneNumber = "";
        Customer customer = new Customer(UUID.randomUUID(), "Linda",phoneNumber);

        given(phoneNumberValidator.test(phoneNumber)).willReturn(false);
        //....Given a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //... When Throw an IllegalException
        assertThatThrownBy(()-> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Phone number " + phoneNumber + " is not valid");

        //Then
        then(customerRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldSaveCustomerIfIdIsNull() {
        //Given
        Customer customer = new Customer("Jiaxuan", "123456");
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //When
        given(phoneNumberValidator.test(customer.getPhoneNumber())).willReturn(true);
        given(customerRepository.selectCustomerByPhoneNumber(customer.getPhoneNumber())).willReturn(Optional.empty());
        underTest.registerNewCustomer(request);

        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer).usingRecursiveComparison().ignoringActualNullFields().isEqualTo(customer);
    }


}