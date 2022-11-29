package com.demo.testpractice.stripe;

import com.demo.testpractice.payment.CardPaymentCharge;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

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
        // crate a new charge
        String source = "0X0X0X0";
        BigDecimal amount = new BigDecimal("10.00");
        String description = "Donation";
        Currency currency = Currency.AUD;

        //... any charge use setPaid() function will return true.
        Charge charge = new Charge();
        charge.setPaid(true);
        given(stripeApi.create(anyMap(), any())).willReturn(charge);

        //When... test started
        underTest.chargeCard(amount, currency, source, description);
        //Then
        //... use @Captor to capture the requestMap and Options
        ArgumentCaptor<Map<String, Object>> mapArgumentCaptor
                = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<RequestOptions> optionArgumentCaptor
                = ArgumentCaptor.forClass(RequestOptions.class);

        // mock stripeApi should create new map and options
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

    @Test
    void itShouldChargeCardButFail() throws StripeException {
        //Given
        // crate a new charge
        String source = "0X0X0X0";
        BigDecimal amount = new BigDecimal("10.00");
        String description = "Donation";
        Currency currency = Currency.AUD;

        //... any charge use setPaid() function will return true.
        Charge charge = new Charge();
        charge.setPaid(false);
        given(stripeApi.create(anyMap(), any())).willReturn(charge);

        //When... test started
        CardPaymentCharge cardPaymentCharge =  underTest.chargeCard(amount, currency, source, description);
        //Then
        //... use @Captor to capture the requestMap and Options
        ArgumentCaptor<Map<String, Object>> mapArgumentCaptor
                = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<RequestOptions> optionArgumentCaptor
                = ArgumentCaptor.forClass(RequestOptions.class);

        // mock stripeApi should create new map and options
        then(stripeApi).should().create(mapArgumentCaptor.capture(), optionArgumentCaptor.capture());

        Map<String, Object> requestMap = mapArgumentCaptor.getValue();
        assertThat(requestMap.keySet()).hasSize(4);
        assertThat(requestMap.get("amount")).isEqualTo(amount);
        assertThat(requestMap.get("currency")).isEqualTo(currency);
        assertThat(requestMap.get("description")).isEqualTo(description);
        assertThat(requestMap.get("source")).isEqualTo(source);

        RequestOptions options = optionArgumentCaptor.getValue();

        assertThat(options).isNotNull();
        assertThat(cardPaymentCharge.isCardDebited()).isFalse();
    }

    @Test
    void itShouldNotChargeIfExceptionThrown() throws StripeException {
        //Given
        // crate a new charge
        String source = "0X0X0X0";
        BigDecimal amount = new BigDecimal("10.00");
        String description = "Donation";
        Currency currency = Currency.RMB;

        //... any charge use setPaid() function will return true.
        Charge charge = new Charge();

        //When... test started
        // Throw exception when stripe api is called
        StripeException stripeException = mock(StripeException.class);
        doThrow(stripeException).when(stripeApi).create(anyMap(), any());
        //then
        //... Test when the function is called, an exception will be thrown
        assertThatThrownBy(()->underTest.chargeCard(amount, currency, source, description))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot make stripe charge");

    }
}