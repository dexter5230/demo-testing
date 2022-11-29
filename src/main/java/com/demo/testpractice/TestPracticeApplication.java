package com.demo.testpractice;

import com.demo.testpractice.customer.Customer;
import com.demo.testpractice.customer.CustomerRepository;
import com.demo.testpractice.payment.Currency;
import com.demo.testpractice.payment.Payment;
import com.demo.testpractice.payment.PaymentRequest;
import com.demo.testpractice.payment.PaymentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
public class TestPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestPracticeApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customerRepository, PaymentService paymentService) {
		return args -> {
			Customer customer  = new Customer(UUID.randomUUID(), "Jiauan", "000");
			customerRepository.save(customer);
			PaymentRequest request = new PaymentRequest(new Payment(1L, customer, new BigDecimal(11.11), Currency.AUD,"Card123" ,"Donation"));
			paymentService.chargeCard(request);

		};


	}
}
