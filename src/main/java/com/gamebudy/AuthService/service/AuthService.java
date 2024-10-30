package com.gamebudy.AuthService.service;

import com.gamebudy.AuthService.dto.*;
import com.gamebudy.AuthService.exception.results.DataResult;
import com.gamebudy.AuthService.exception.results.ErrorDataResult;
import com.gamebudy.AuthService.exception.results.SuccessDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final String userServiceUrl;

    @Autowired
    public AuthService(RestTemplate restTemplate, PasswordEncoder passwordEncoder,
                       @Value("${user.service.url}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
        this.userServiceUrl = userServiceUrl;
    }

    public DataResult<RegisterResponse> register(RegisterRequest registerRequest) {
        if (!isPasswordValid(registerRequest.getPassword())) {
            return new ErrorDataResult<>("Password must be between 8 and 16 characters.");
        }

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        HttpEntity<RegisterRequest> request = new HttpEntity<>(registerRequest);

        ResponseEntity<DataResult<RegisterResponse>> response = restTemplate.exchange(
                userServiceUrl + "/users/register",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<DataResult<RegisterResponse>>() {}
        );

        return response.getBody();
    }


    public DataResult<LoginResponse> login(LoginRequest loginRequest) {
        String findUserUrl = userServiceUrl + "/users/find?userName=" + loginRequest.getUserName();

        ResponseEntity<DataResult<LoginResponse>> response = restTemplate.exchange(
                findUserUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<DataResult<LoginResponse>>() {}
        );

        LoginResponse foundUser = response.getBody().getData();

        String matchPasswordUrl = userServiceUrl + "/users/match-password?username=" + loginRequest.getUserName() + "&password=" + loginRequest.getPassword();

        ResponseEntity<PasswordResponse> passwordResponse = restTemplate.exchange(
                matchPasswordUrl,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<PasswordResponse>() {}
        );

        if (!Boolean.TRUE.equals(passwordResponse.getBody().isSuccess())) {
            return new ErrorDataResult<>("Invalid username or password.");
        }

        return new SuccessDataResult<>(foundUser, "User retrieved successfully.");
    }


    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 16;
    }

}
