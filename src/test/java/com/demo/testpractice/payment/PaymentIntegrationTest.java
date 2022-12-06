package com.demo.testpractice.payment;

import com.demo.testpractice.customer.Customer;
import com.demo.testpractice.customer.CustomerRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PaymentIntegrationTest {
    /** @noinspection SpringJavaInjectionPointsAutowiringInspection*/
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        //Given
        //create a customer
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "Jiaxuan", "0000");
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);
        ResultActions customerRegResultActions = mockMvc
                .perform(post("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(customerRegistrationRequest)))) ;

        //When
        Payment payment = new Payment(1L,customer,
                                                new BigDecimal("11.11"),
                                                Currency.AUD, "xixix",
                                      "Donation");
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        //given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
        ResultActions paymentResultActions = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(paymentRequest))));
        //Then
        customerRegResultActions.andExpect(status().isOk());
        paymentResultActions.andExpect(status().isOk());

    }

    private String objectToJson(Object object) {
        try{
            return new ObjectMapper().writeValueAsString(object);
        } catch(JsonProcessingException e) {
            fail("Fail to convert object to json");
            return null;
        }
    }
}
