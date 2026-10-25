package com.example.spring_security_user_details_service.controller;

import com.example.spring_security_user_details_service.DTO.LoginDTO;
import com.example.spring_security_user_details_service.DTO.RegisterDTO;
import com.example.spring_security_user_details_service.DTO.ResponseDTO;
import com.example.spring_security_user_details_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Valid
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // POST MAPPING FOR REGISTRATION
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody RegisterDTO register) {
        ResponseDTO registerUser = userService.createUser(register);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
    }

    // USER LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO request) {
        System.out.println(request.getUsername());
        userService.loginUser(request);
        System.out.println(request.getUsername());
        return ResponseEntity.ok("Login successfully");
    }

}
