package com.codewordle.codewordle.mapper;

import com.codewordle.codewordle.dto.UserCreateRequest;
import com.codewordle.codewordle.dto.UserResponse;
import com.codewordle.codewordle.model.User;
import org.mapstruct.Mapper;

/**
 * Mapper for the user entity and its DTOs.
 * Uses MapStruct to generate implementation code at compile time.
 * The componentModel = "spring" attribute makes the generated mapper a Spring bean.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps a UserCreateRequest DTO to a User entity.
     * @param dto the user creation request DTO
     * @return the mapped User entity
     */
    User toEntity(UserCreateRequest dto);

    /**
     * Maps a User entity to a UserResponse DTO.
     * @param user the user entity
     * @return the mapped UserResponse DTO
     */
    UserResponse toDto(User user);
}