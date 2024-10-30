package com.gamebudy.AuthService.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LoginResponse {
    private String userId;
    private String userName;
    private String email;
    private Gender gender;
    private Integer age;
    private String profilePhoto;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> preferredLanguages;
    private boolean premium;
}

