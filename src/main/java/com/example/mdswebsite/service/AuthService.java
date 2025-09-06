package com.example.mdswebsite.service;

import com.example.mdswebsite.dto.AuthDTO;


public interface AuthService {
    AuthDTO.AuthResponse  register(AuthDTO.RegisterRequest req);

    AuthDTO.AuthResponse login(AuthDTO.RegisterRequest req);

    AuthDTO.AuthResponse login(AuthDTO.LoginRequest req);

    AuthDTO.RefreshResponse refresh(String refreshToken);

    void sendMagicLink(String email);
}
