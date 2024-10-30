package com.gamebudy.AuthService.dto;

import lombok.Data;

@Data
public class PasswordResponse {
    private boolean success;
    private String message;
}
