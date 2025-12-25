package com.example.bookstore.service.impl;

import com.example.bookstore.dto.AuthResponse;
import com.example.bookstore.dto.LoginRequest;
import com.example.bookstore.entity.Admin;
import com.example.bookstore.entity.Customer;
import com.example.bookstore.repository.AdminRepository;
import com.example.bookstore.repository.CustomerRepository;
import com.example.bookstore.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AdminRepository adminRepo;
    private final CustomerRepository customerRepo;

    public AuthServiceImpl(AdminRepository adminRepo, CustomerRepository customerRepo) {
        this.adminRepo = adminRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        if ("ADMIN".equalsIgnoreCase(req.role())) {
            // 1. Check Admin Table
            Admin admin = adminRepo.findByUsername(req.username()).orElseThrow(() -> new RuntimeException("Admin not found"));

            // 2. Validate Password (Plain text for this project)
            if (!admin.getPassword().equals(req.password())) {
                throw new RuntimeException("Invalid credentials");
            }

            return new AuthResponse(admin.getAdminId(), admin.getUsername(), "ADMIN", admin.getName());

        } else {
            // 1. Check Customer Table
            Customer cust = customerRepo.findByUsername(req.username()).orElseThrow(() -> new RuntimeException("Customer not found"));

            // 2. Validate Password
            if (!cust.getPassword().equals(req.password())) {
                throw new RuntimeException("Invalid credentials");
            }

            return new AuthResponse(cust.getCustomerId(), cust.getUsername(), "CUSTOMER", cust.getFirstName());
        }
    }
}