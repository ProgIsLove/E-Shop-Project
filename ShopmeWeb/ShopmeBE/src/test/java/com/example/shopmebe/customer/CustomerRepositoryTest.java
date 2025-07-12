package com.example.shopmebe.customer;

import com.example.shopmebe.ShopmeBeApplication;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.PhoneNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ShopmeBeApplication.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void existsByEmailIgnoreCaseAndIdNot() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Losh");
        customer.setPassword("password");
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhone("999 332 321");
        customer.setPhoneNumber(phoneNumber);
        customer.setId(1000);
        customer.setAddressLine1("Address 1");
        customer.setCity("City");
        customer.setEmail("test@test.com");
        customer.setPostalCode("800");
        customer.setState("State");

        customerRepository.save(customer);

        boolean result = customerRepository.existsByEmailIgnoreCaseAndIdNot("test@test.com", 1000);

        assertTrue(result);
    }

}
