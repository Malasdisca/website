package com.example.mdswebsite.service;

import com.example.mdswebsite.entity.User;

public interface UserService {

    User getById(Long id);

    User getByEmail(String email);
}
