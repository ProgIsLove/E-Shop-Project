package com.example.shopmefe.customer;


import com.example.shopmefe.exception.ConflictException;
import com.shopme.common.entity.Customer;
import net.bytebuddy.utility.RandomString;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isEmailUnique(@NotNull String email) throws ConflictException {
        if (customerRepository.findByEmail(email).isPresent()) {
            throw new ConflictException(String.format("There is another user having the email %s", email));
        }

        return true;
    }

    public void registerCustomer(Customer customer) {
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());

        String randomCode = RandomString.make(10);
        customer.setVerificationCode(randomCode);

        customerRepository.save(customer);
    }

    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public boolean verify(String verificationCode) {
        return customerRepository
                .findByVerificationCode(verificationCode)
                .filter(customer -> !customer.isEnabled())
                .map(customer -> {
                    customerRepository.updateEnabledStatus(customer.getId());
                    return true;
                })
                .orElse(false);
    }
}
