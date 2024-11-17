package com.example.shopmefe.customer;

import com.example.shopmefe.api.countryapi.CountryAPI;
import com.shopme.common.entity.Customer;
import com.example.shopmefe.api.countryapi.CountryResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class CustomerController {

    private final CustomerService customerService;
    private final CountryAPI countryAPI;

    public CustomerController(CustomerService customerService, CountryAPI countryAPI) {
        this.customerService = customerService;
        this.countryAPI = countryAPI;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        List<CountryResponse> countries = countryAPI.countries().block();

        model.addAttribute("pageTitle", "Customer Register");
        model.addAttribute("listCountries", countries);
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }
}
