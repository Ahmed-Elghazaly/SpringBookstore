package com.example.bookstore.service;

import com.example.bookstore.dto.AuthResponse;
import com.example.bookstore.dto.LoginRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
