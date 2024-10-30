package com.gamebudy.AuthService.controller;

import com.gamebudy.AuthService.dto.LoginRequest;
import com.gamebudy.AuthService.dto.LoginResponse;
import com.gamebudy.AuthService.dto.RegisterRequest;
import com.gamebudy.AuthService.dto.RegisterResponse;
import com.gamebudy.AuthService.exception.results.DataResult;
import com.gamebudy.AuthService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<DataResult<RegisterResponse>> register(@RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<DataResult<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }
}