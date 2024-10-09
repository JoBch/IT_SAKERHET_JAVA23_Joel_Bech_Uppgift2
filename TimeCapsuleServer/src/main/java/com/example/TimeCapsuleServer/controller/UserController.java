package com.example.TimeCapsuleServer.controller;

import com.example.TimeCapsuleServer.entities.UserEntity;
import com.example.TimeCapsuleServer.services.UserService;
import com.example.TimeCapsuleServer.utils.JWTUtil;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

//No checking for token here because we have to able to access this without a valid token
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, String> registerUser(@RequestBody UserEntity newUser) {
        try {
            userService.registerUser(newUser);
            return Map.of("message", "User registered successfully!");
        } catch (IllegalArgumentException e) {
            return Map.of("error", "User with this email already exists!");
        }
    }

    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody Map<String, Object> credentials, HttpSession session) throws JOSEException {
        String email = (String) credentials.get("email");
        String password = (String) credentials.get("password");

        boolean loginSuccessful = userService.loginUser(email, password, session);
        if (loginSuccessful) {

            String token = JWTUtil.generateToken(email);

            //Return token in response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response).getBody();
        } else {
            return Map.of("error", "Invalid email or password");
        }
    }
}
