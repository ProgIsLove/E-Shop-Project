package com.example.shopmefe.customer;

import com.example.shopmefe.api.countryapi.CountryAPI;
import com.example.shopmefe.api.countryapi.CountryResponse;
import com.example.shopmefe.setting.EmailSettingBag;
import com.example.shopmefe.setting.SettingService;
import com.example.shopmefe.utility.MailUtil;
import com.shopme.common.entity.Customer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.util.List;


@Controller
@Slf4j
public class CustomerController {

    private final CustomerService customerService;
    private final CountryAPI countryAPI;
    private final SettingService settingService;

    public CustomerController(CustomerService customerService, CountryAPI countryAPI, SettingService settingService) {
        this.customerService = customerService;
        this.countryAPI = countryAPI;
        this.settingService = settingService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        List<CountryResponse> countries = countryAPI.countries().block();
        
        model.addAttribute("pageTitle", "Customer Register");
        model.addAttribute("listCountries", countries);
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }

    @PostMapping("/customer")
    public String createCustomer(Customer customer,
                                 Model model,
                                 HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(request, customer);

        model.addAttribute("pageTitle", "Registration Succeeded!");

        return "register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSetting = settingService.getEmailSetting();
        JavaMailSenderImpl mailSender = MailUtil.prepareMailSender(emailSetting);

        String toAddress = customer.getEmail();
        String subject = emailSetting.getCustomerVerifySubject();
        String content = emailSetting.getCustomerVerifySubject();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom(emailSetting.getFromAddress(), emailSetting.getSenderName());
        messageHelper.setTo(toAddress);
        messageHelper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFullName());

//        String verifyUrl = MailUtil.getSiteUrl(request) + "/verify?code=" + customer.getVerificationCode();

        String verifyUrl = ServletUriComponentsBuilder
                .fromRequestUri(request)
                .replacePath("/verify")
                .replaceQuery(null)
                .queryParam("code", customer.getVerificationCode())
                .build()
                .encode()
                .toUriString();

        content = content.replace("[[URL]]", verifyUrl);

        messageHelper.setText(content, true);

        mailSender.send(message);

        log.info("To address: {}", toAddress);
        log.info("To verifyUrl: {}", verifyUrl);

    }
}
