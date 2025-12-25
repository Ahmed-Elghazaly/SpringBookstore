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
            throw new IllegalArgumentException("Username already exists");
        }
        if (customerRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Customer customer = new Customer(request.username(), request.password(), request.email(), request.firstName(), request.lastName(), request.shippingAddress(), request.phoneNumber());

        Customer saved = customerRepository.save(customer);

        return new CustomerResponse(saved.getCustomerId(), saved.getUsername(), saved.getEmail(),
                saved.getFirstName(), saved.getLastName(), saved.getShippingAddress(), saved.getPhoneNumber());    }

    @Override
    public CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (request.password() != null && !request.password().isBlank()) {
            customer.setPassword(request.password());
        }
        if (request.email() != null) {
            customer.setEmail(request.email());
        }
        if (request.firstName() != null) {
            customer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            customer.setLastName(request.lastName());
        }
        if (request.shippingAddress() != null) {
            customer.setShippingAddress(request.shippingAddress());
        }
        if (request.phoneNumber() != null) {
            customer.setPhoneNumber(request.phoneNumber());
        }

        Customer saved = customerRepository.save(customer);

        return new CustomerResponse(saved.getCustomerId(), saved.getUsername(), saved.getEmail(),
                saved.getFirstName(), saved.getLastName(), saved.getShippingAddress(), saved.getPhoneNumber());    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        return new CustomerResponse(customer.getCustomerId(), customer.getUsername(), customer.getEmail(),
                customer.getFirstName(), customer.getLastName(), customer.getShippingAddress(), customer.getPhoneNumber());    }
}