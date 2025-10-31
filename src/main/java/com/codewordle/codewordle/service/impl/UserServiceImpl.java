package com.codewordle.codewordle.service.impl;

import com.codewordle.codewordle.dto.UserResponse;
import com.codewordle.codewordle.exception.ResourceNotFoundException;
import com.codewordle.codewordle.mapper.UserMapper;
import com.codewordle.codewordle.model.User;
import com.codewordle.codewordle.repository.UserRepository;
import com.codewordle.codewordle.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse getById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }
}
