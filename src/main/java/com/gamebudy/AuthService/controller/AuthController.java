package com.gamebudy.AuthService.controller;

import com.gamebudy.AuthService.dto.LoginRequest;
import com.gamebudy.AuthService.dto.LoginResponse;
import com.gamebudy.AuthService.dto.RegisterRequest;
import com.gamebudy.AuthService.dto.RegisterResponse;
import com.gamebudy.AuthService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

}