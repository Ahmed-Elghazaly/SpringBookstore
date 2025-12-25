package com.example.bookstore.controller;

import com.example.bookstore.dto.CreateCustomerRequest;
import com.example.bookstore.dto.CustomerResponse;
import com.example.bookstore.dto.UpdateCustomerRequest;
import com.example.bookstore.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse register(@Valid @RequestBody CreateCustomerRequest request) {
        return customerService.register(request);
    }

    @GetMapping("/{customerId}")
    public CustomerResponse getProfile(@PathVariable Long customerId) {
        return customerService.getProfile(customerId);
    }

    @PutMapping("/{customerId}")
    public CustomerResponse updateProfile(@PathVariable Long customerId, @Valid @RequestBody UpdateCustomerRequest request) {
        return customerService.updateProfile(customerId, request);
    }
}
