package com.example.mdswebsite.controller;

import com.example.mdswebsite.dto.AuthDTO;
import com.example.mdswebsite.entity.User;
import com.example.mdswebsite.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mds/auth")
@RequiredArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDTO.RegisterRequest req) {
        AuthDTO.AuthResponse u = authService.register(req);
        return ResponseEntity.ok(u);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTO.LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody AuthDTO.RefreshRequest req) {
        return ResponseEntity.ok(authService.refresh(req.refreshToken()));
    }

    @PostMapping("/magic-link")
    public ResponseEntity<?> magicLink(@Valid @RequestBody AuthDTO.MagicLinkRequest req) {
        authService.sendMagicLink(req.email());
        return ResponseEntity.accepted().build();
    }
}