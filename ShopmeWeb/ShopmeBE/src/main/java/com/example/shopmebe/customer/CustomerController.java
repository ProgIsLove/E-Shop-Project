package com.example.shopmebe.customer;

import com.example.shopmebe.exception.CustomerNotFoundException;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String getFirstPage(Model model) {
        return listByPage(model, 1, "firstName", "asc", null);
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(Model model,
                             @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword) {
        Page<Customer> page = customerService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Customer> listCustomers = page.getContent();

        long startCount = (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;

        long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "customers/customers";
    }

    @GetMapping("/customers/{customerId}/enabled/{status}")
    public String updateCustomerEnabledStatus(@PathVariable("customerId") Integer customerId,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {

        customerService.updateCustomerEnabledStatus(customerId, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The customer with id " + customerId + " has been enabled" + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/customers";
    }

    @GetMapping("/customers/detail/{customerId}")
    public String viewCustomer(@PathVariable("customerId") Integer customerId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.findById(customerId);
            model.addAttribute("customer", customer);

            return "customers/customer_detail_modal";
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("/customers/delete/{customerId}")
    public String deleteCustomer(@PathVariable("customerId") Integer customerId, RedirectAttributes redirectAttributes) throws CustomerNotFoundException {
        customerService.delete(customerId);
        redirectAttributes.addFlashAttribute("message", "Customer with id " + customerId + " has been successfully deleted");
        return "redirect:/customers";
    }

    @GetMapping("/customers/edit/{customerId}")
    public String editCustomer(@PathVariable("customerId") Integer customerId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.findById(customerId);
            List<Country> countries = customerService.listAllCountries();

            model.addAttribute("listCountries", countries);
            model.addAttribute("customer", customer);
            model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)", customerId));

            return "customers/customer_form";

        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes) throws CustomerNotFoundException {
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("message", "Customer with id " + customer.getId() + " has been successfully saved");
        return "redirect:/customers";
    }
}
