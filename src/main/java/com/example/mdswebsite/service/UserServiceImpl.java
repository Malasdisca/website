package com.example.mdswebsite.service;

import com.example.mdswebsite.entity.User;
import com.example.mdswebsite.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository users;


    public UserServiceImpl(UserRepository users) {
        this.users = users;
    }

    @Override
    public User getById(Long id) {
        return users.findById(id).orElseThrow(()->new NoSuchElementException("user_not_found"));
    }


    @Override
    public User getByEmail(String email) {
        return users.findByEmail(email).orElseThrow();
    }

}
