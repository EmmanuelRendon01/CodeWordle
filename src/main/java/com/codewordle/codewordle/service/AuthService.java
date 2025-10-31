package com.codewordle.codewordle.service;

import com.codewordle.codewordle.dto.LoginRequest;
import com.codewordle.codewordle.dto.LoginResponse;
import com.codewordle.codewordle.dto.UserCreateRequest;
import com.codewordle.codewordle.dto.UserResponse;
import com.codewordle.codewordle.model.User;

public interface AuthService {

    public UserResponse Register(UserCreateRequest newUser);
    public LoginResponse Login(LoginRequest request);
}
