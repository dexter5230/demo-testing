package com.demo.testpractice.utiles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class PhoneNumberValidatorTest {
    private PhoneNumberValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new PhoneNumberValidator();
    }

    @ParameterizedTest
    @CsvSource({"+61403562186,TRUE","+8618924420210, FALSE","+999999, FALSE"})
    void itShouldValidatePhoneNumber(String phoneNumber, boolean response) {
        //Given
        assertThat(underTest.test(phoneNumber)).isEqualTo(response);
        //When
        //Then
    }
}