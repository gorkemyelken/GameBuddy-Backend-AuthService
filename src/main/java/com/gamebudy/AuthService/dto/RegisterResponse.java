package com.gamebudy.AuthService.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterResponse {
    private String id;
    private String userName;
    private String email;
    private Gender gender;
    private Integer age;
    private String profilePhoto;
    private boolean isPremium;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
