package com.gamebudy.AuthService.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userName;

    private String email;

    private String password;

    private Gender gender;

    private Integer age;
}
