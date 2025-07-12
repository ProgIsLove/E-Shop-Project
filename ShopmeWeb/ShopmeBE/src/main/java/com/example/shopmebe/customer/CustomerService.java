package com.example.shopmebe.customer;

import com.example.shopmebe.exception.CustomerNotFoundException;
import com.example.shopmebe.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class CustomerService {
    public static final int CUSTOMERS_PER_PAGE = 10;

    private final CustomerRepository customerRepository;
    private final CountryRepository countryRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository,
                           CountryRepository countryRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.countryRepository = countryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        PageRequest pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);

        if (keyword != null) {
            return customerRepository.findAll(keyword, pageable);
        }

        return customerRepository.findAll(pageable);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
        customerRepository.updateEnabledStatus(id, enabled);
    }

    public Customer findById(Integer customerId) throws CustomerNotFoundException {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Could not find any customersId: %d".formatted(customerId)));
    }

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(Integer customerId, String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        return !customerRepository.existsByEmailIgnoreCaseAndIdNot(email, customerId);
    }

    public void save(Customer customerInForm) throws CustomerNotFoundException {
        if (!customerInForm.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
            customerInForm.setPassword(encodedPassword);
        } else {
            Customer customer = this.findById(customerInForm.getId());
            customerInForm.setPassword(customer.getPassword());
        }
        customerRepository.save(customerInForm);
    }

    public void delete(Integer customerId) throws CustomerNotFoundException {
        Long countById = customerRepository.countById(customerId);
        if (countById == 0) {
            throw new CustomerNotFoundException("Could not find any customersId: %d".formatted(customerId));
        }
        customerRepository.deleteById(customerId);
    }
}
