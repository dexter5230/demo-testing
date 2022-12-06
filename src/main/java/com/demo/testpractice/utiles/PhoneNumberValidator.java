package com.demo.testpractice.utiles;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
@Service
public class PhoneNumberValidator implements Predicate<String> {
    @Override
    public boolean test(String phoneNumber) {
        return phoneNumber.length() == 12 && phoneNumber.startsWith("+614");
    }
}
