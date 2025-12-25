package com.example.bookstore.service.impl;

import com.example.bookstore.dto.CreateCustomerRequest;
import com.example.bookstore.dto.CustomerResponse;
import com.example.bookstore.dto.UpdateCustomerRequest;
import com.example.bookstore.entity.Customer;
import com.example.bookstore.repository.CustomerRepository;
import com.example.bookstore.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse register(CreateCustomerRequest request) {
        if (customerRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }
        if (customerRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }
        Customer customer = new Customer(request.username(), request.password(), request.email(),
                request.firstName(), request.lastName(), request.shippingAddress(), request.phoneNumber());
        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getProfile(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return toResponse(customer);
    }

    @Override
    public CustomerResponse updateProfile(Long customerId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        if (request.password() != null && !request.password().isBlank()) customer.setPassword(request.password());
        if (request.email() != null) customer.setEmail(request.email());
        if (request.firstName() != null) customer.setFirstName(request.firstName());
        if (request.lastName() != null) customer.setLastName(request.lastName());
        if (request.shippingAddress() != null) customer.setShippingAddress(request.shippingAddress());
        if (request.phoneNumber() != null) customer.setPhoneNumber(request.phoneNumber());
        return toResponse(customerRepository.save(customer));
    }

    private CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(c.getCustomerId(), c.getUsername(), c.getEmail(),
                c.getFirstName(), c.getLastName(), c.getShippingAddress(), c.getPhoneNumber());
    }
}
