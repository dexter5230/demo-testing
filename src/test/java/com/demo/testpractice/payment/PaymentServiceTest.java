package com.demo.testpractice.payment;

import com.demo.testpractice.customer.Customer;
import com.demo.testpractice.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

class PaymentServiceTest {
    private PaymentService underTest;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private FakeCardPaymentCharger cardPaymentCharger;
    @Captor
    private ArgumentCaptor<Payment> paymentCapture;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new PaymentService(customerRepository,paymentRepository,cardPaymentCharger);
    }

    @Test
    void itShouldSavePayment() {
        //Given
        Customer customer = new Customer(UUID.randomUUID(),"Jiaxuan", "000");
        Payment payment = new Payment(1L, customer, new BigDecimal("11.01"), Currency.AUD, "Card123","donation");
        PaymentRequest request = new PaymentRequest(payment);
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(mock(Customer.class)));




        //When
        given(cardPaymentCharger.chargeCard(payment.getAmount(), payment.getCurrency(), payment.getSource(), payment.getDescription()))
                .willReturn(new CardPaymentCharge(true));
        //Then
        underTest.chargeCard(request);
        then(paymentRepository).should().save(paymentCapture.capture());

        assertThat(paymentCapture.getValue()).usingRecursiveComparison().ignoringFields("customerId").isEqualTo(payment);
        //assertThat(paymentCapture.getValue().getCustomer().getId()).isEqualTo(customer.getId());
    }

    @Test
    void itShouldNotSavePaymentIfCustomerNotFound() {

        //Given
        Customer customer = new Customer(UUID.randomUUID(),"Jiaxuan", "000");
        Payment payment = new Payment(1L, customer, new BigDecimal("11.01"), Currency.valueOf("USD"), "Card123","donation");
        PaymentRequest request = new PaymentRequest(payment);
        given(customerRepository.findById(customer.getId())).willReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() ->underTest.chargeCard(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The customer with Id " + customer.getId() + " does not exist in the database");
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotSavePayementIfCardChargerFailed() {
        //Given
        Customer customer = new Customer(UUID.randomUUID(),"Jiaxuan", "000");
        Payment payment = new Payment(1L, customer, new BigDecimal("11.01"), Currency.AUD, "Card123","donation");
        PaymentRequest request = new PaymentRequest(payment);
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //When
        given(cardPaymentCharger.chargeCard(payment.getAmount(), payment.getCurrency(), payment.getSource(), payment.getDescription()))
                .willReturn(new CardPaymentCharge(false));

        //Then
        assertThatThrownBy(()-> underTest.chargeCard(request)).isInstanceOf(IllegalStateException.class).hasMessageContaining("The debited is false");
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotSavePaymentIfCurrencyIsNotSupported() {
        //Given
        Customer customer = new Customer(UUID.randomUUID(),"Jiaxuan", "000");
        Payment payment = new Payment(1L, customer, new BigDecimal("11.01"), Currency.USD, "Card123","donation");
        PaymentRequest request = new PaymentRequest(payment);
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(mock(Customer.class)));

        //When
        //Then
        assertThatThrownBy(()-> underTest.chargeCard(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The currency " + payment.getCurrency() + " is not supported");
        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotSavePaymentIfCustomerIsNull() {
        //Given
        Payment payment = new Payment(1L, null, new BigDecimal("11.01"), Currency.AUD, "Card123","donation");
        PaymentRequest request = new PaymentRequest(payment);

        //When
        assertThatThrownBy(()-> underTest.chargeCard(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("The request of customer with payment id [%s] is null", request.getPayment().getPaymentId().toString()));
        //Then
        then(paymentRepository).should(never()).save(any(Payment.class));
    }
}