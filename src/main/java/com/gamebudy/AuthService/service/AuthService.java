package com.gamebudy.AuthService.service;

import com.gamebudy.AuthService.dto.LoginRequest;
import com.gamebudy.AuthService.dto.LoginResponse;
import com.gamebudy.AuthService.dto.RegisterRequest;
import com.gamebudy.AuthService.dto.RegisterResponse;
import com.gamebudy.AuthService.exception.BadCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

    public RegisterResponse register(RegisterRequest registerRequest) {
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        HttpEntity<RegisterRequest> request = new HttpEntity<>(registerRequest);

        ResponseEntity<RegisterResponse> response = restTemplate.exchange(
                userServiceUrl + "/users/register",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<RegisterResponse>() {}
        );

        return response.getBody();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        // Kullanıcıyı bulmak için User Service'e istek yap
        String findUserUrl = userServiceUrl + "/users/find?userName=" + loginRequest.getUserName();

        ResponseEntity<LoginResponse> response = restTemplate.exchange(
                findUserUrl,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<LoginResponse>() {}
        );

        // Kullanıcı bulunamazsa veya yanıt alınamazsa hata fırlat
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        LoginResponse foundUser = response.getBody();

        // Şifre kontrolü yapmak için UserService'e istek at
        String matchPasswordUrl = userServiceUrl + "/users/match-password?username=" + loginRequest.getUserName() + "&password=" + loginRequest.getPassword();

        ResponseEntity<Boolean> passwordResponse = restTemplate.exchange(
                matchPasswordUrl,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<Boolean>() {}
        );

        // Şifre eşleşmiyorsa hata fırlat
        if (!Boolean.TRUE.equals(passwordResponse.getBody())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Başarılı bir giriş durumunda dönecek yanıtı oluşturun
        return foundUser; // İstediğiniz bilgileri burada döndürün.
    }

}
