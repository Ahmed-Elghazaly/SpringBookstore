package com.example.bookstore.service;

import com.example.bookstore.dto.CreateCustomerRequest;
import com.example.bookstore.dto.CustomerResponse;
import com.example.bookstore.dto.UpdateCustomerRequest;

public interface CustomerService {


    CustomerResponse register(CreateCustomerRequest request);
    CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request);
    CustomerResponse getCustomer(Long customerId);
}
