package com.rebatosoft.minispotify.dto.requests;

import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
        String username,
        String foto
)
{}