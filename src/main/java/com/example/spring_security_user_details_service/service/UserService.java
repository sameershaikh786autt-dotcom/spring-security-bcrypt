package com.example.spring_security_user_details_service.service;


import com.example.spring_security_user_details_service.GlobalExceptions.AlreadyExistsException;
import com.example.spring_security_user_details_service.enums.Roles;
import com.example.spring_security_user_details_service.models.User;
import com.example.spring_security_user_details_service.DTO.RegisterDTO;
import com.example.spring_security_user_details_service.DTO.ResponseDTO;

import com.example.spring_security_user_details_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseDTO createUser(RegisterDTO register) {
        // USER REGISTER ASSIGN TO DTO TO ENTITY
        User registerUser = DtoToEntity(register);

        // SAVING THE DATA INTO A ENTITY
        User saved = savedUser(registerUser);

        // FOR RETURN DATA INTO
        ResponseDTO response = EntityToDTO(saved);

        return response;
    }

    // CREATING METHOD
    private User DtoToEntity(RegisterDTO register)  {
        if (userRepository.existsByUsername(register.getUsername())){
            throw new AlreadyExistsException("User Already exists: " +  register.getUsername());
        }

        if (userRepository.existsByEmail(register.getEmail())) {
            throw new AlreadyExistsException("User Already exists: " +  register.getEmail());
        }

        User user = User.builder()
                .username(register.getUsername())
                .fullName(register.getFullName())
                .password(passwordEncoder.encode(register.getPassword()))
                .role(Roles.USER)
                .email(register.getEmail())
                .build();
        return user;
    }


    // CREATING METHOD FOR SAVING
  private User savedUser(User user) {
        return userRepository.save(user);
  }

    // CREATING METHOD FOR RESPONSE
    private ResponseDTO EntityToDTO(User user) {
        ResponseDTO response = ResponseDTO.builder()
                .message("Successfully created user")
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreateDate())
                .build();
       return response;
    }
}
