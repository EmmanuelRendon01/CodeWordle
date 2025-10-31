package com.codewordle.codewordle.service;

import com.codewordle.codewordle.dto.UserResponse;

public interface UserService {
    public UserResponse getById(int id);
}
