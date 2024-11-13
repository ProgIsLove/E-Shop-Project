package com.example.shopmefe.customer;

import com.example.shopmebe.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CountryRepository countryRepository;

    public CustomerService(CustomerRepository customerRepository, CountryRepository countryRepository) {
        this.customerRepository = customerRepository;
        this.countryRepository = countryRepository;
    }

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }
}
