package com.example.mdswebsite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDTO {

    // Requests
    public record RegisterRequest(
            @Email @NotBlank String email,
            @NotBlank @Size(min = 8, max = 128) String password,
            @NotBlank String name
    ) {}

    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}

    public record RefreshRequest(
            @NotBlank String refreshToken
    ) {}

    public record MagicLinkRequest(
            @Email @NotBlank String email
    ) {}

    // Responses
    public record UserView(
            Long id,
            String email,
            String name
    ) {}

    public record TokenPair(
            String accessToken,
            String refreshToken,
            long expiresIn // seconds
    ) {}

    public record AuthResponse(
            UserView user,
            TokenPair tokens
    ) {}

    public record RefreshResponse(
            String accessToken,
            long expiresIn // seconds
    ) {}
}
