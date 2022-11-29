package com.demo.testpractice.stripe;

import com.demo.testpractice.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class StripeServiceTest {
    private StripeService underTest;
    @Mock
    private StripeApi stripeApi;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new StripeService(stripeApi);
    }

    @Test
    void itShouldChargeCard() throws StripeException {
        //Given
        String source = "0X0X0X0";
        BigDecimal amount = new BigDecimal("10.00");
        String description = "Donation";
        Currency currency = Currency.AUD;
        Charge charge = new Charge();
        charge.setPaid(true);
        given(stripeApi.create(anyMap(), any())).willReturn(charge);
        //When
        underTest.chargeCard(amount, currency, source, description);
        //Then
        ArgumentCaptor<Map<String, Object>> mapArgumentCaptor
                = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<RequestOptions> optionArgumentCaptor
                = ArgumentCaptor.forClass(RequestOptions.class);
        then(stripeApi).should().create(mapArgumentCaptor.capture(), optionArgumentCaptor.capture());
        Map<String, Object> requestMap = mapArgumentCaptor.getValue();
        assertThat(requestMap.keySet()).hasSize(4);
        assertThat(requestMap.get("amount")).isEqualTo(amount);
        assertThat(requestMap.get("currency")).isEqualTo(currency);
        assertThat(requestMap.get("description")).isEqualTo(description);
        assertThat(requestMap.get("source")).isEqualTo(source);

        RequestOptions options = optionArgumentCaptor.getValue();

        assertThat(options).isNotNull();



    }
}