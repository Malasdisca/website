package com.example.mdswebsite.service;

import com.example.mdswebsite.dto.AuthDTO;
import com.example.mdswebsite.entity.User;
import com.example.mdswebsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private UserRepository users;
    private PasswordEncoder encoder; // define a bean: new BCryptPasswordEncoder()

    // MVP in-memory refresh store
    private final Map<String, Long> refreshStore = new ConcurrentHashMap<>();
    private static final long ACCESS_TTL_SEC = Duration.ofHours(1).toSeconds();

    @Override
    @Transactional
    public AuthDTO.AuthResponse register(AuthDTO.RegisterRequest req) {
        if (users.existsByEmail(req.email())) throw new IllegalArgumentException("email_taken");
        User u = new User();
        u.setEmail(req.email());
        u.setName(req.name());
        u.setPasswordHash(encoder.encode(req.password()));
        u = users.save(u);
        return issue(u);
    }

    // Keep to satisfy your interface; delegate to the real login
    @Override
    public AuthDTO.AuthResponse login(AuthDTO.RegisterRequest req) {
        return login(new AuthDTO.LoginRequest(req.email(), req.password()));
    }

    @Override
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest req) {
        User u = users.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("bad_credentials"));
        if (!encoder.matches(req.password(), u.getPasswordHash()))
            throw new IllegalArgumentException("bad_credentials");
        return issue(u);
    }

    @Override
    public AuthDTO.RefreshResponse refresh(String refreshToken) {
        Long uid = refreshStore.get(refreshToken);
        if (uid == null) throw new IllegalArgumentException("invalid_refresh");
        User u = users.findById(uid).orElseThrow(() -> new NoSuchElementException("user_not_found"));
        String access = generateAccessToken(u.getId(), u.getEmail());
        return new AuthDTO.RefreshResponse(access, ACCESS_TTL_SEC);
    }

    @Override
    public void sendMagicLink(String email) {
        // no-op MVP; avoid user enumeration
        users.findByEmail(email).orElse(null);
    }

    private AuthDTO.AuthResponse issue(User u) {
        String access = generateAccessToken(u.getId(), u.getEmail());
        String refresh = generateRefreshToken(u.getId());
        return new AuthDTO.AuthResponse(
                new AuthDTO.UserView(u.getId(), u.getEmail(), u.getName()),
                new AuthDTO.TokenPair(access, refresh, ACCESS_TTL_SEC)
        );
    }

    private String generateAccessToken(Long id, String email) {
        return "acc_" + UUID.randomUUID(); // replace with real JWT later
    }

    private String generateRefreshToken(Long id) {
        String rt = "ref_" + UUID.randomUUID();
        refreshStore.put(rt, id);
        return rt;
    }
}
