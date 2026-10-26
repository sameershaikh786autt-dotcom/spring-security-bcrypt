package com.example.spring_security_user_details_service.service;


import com.example.spring_security_user_details_service.DTO.LoginDTO;
import com.example.spring_security_user_details_service.GlobalExceptions.AlreadyExistsException;
import com.example.spring_security_user_details_service.JwtService;
import com.example.spring_security_user_details_service.enums.Roles;
import com.example.spring_security_user_details_service.models.User;
import com.example.spring_security_user_details_service.DTO.RegisterDTO;
import com.example.spring_security_user_details_service.DTO.ResponseDTO;

import com.example.spring_security_user_details_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService  jwtService;

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


    // FOR VALIDATION OF USERNAME AND PASSWORD
    public String loginUser(LoginDTO request) {
        try {
            // 1. User ke plain username aur password ko token me wrap karte hain
            UsernamePasswordAuthenticationToken token
                    = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            // AuthenticationManager background me CustomUserDetailsService aur BCrypt ko trigger karta hai
            Authentication authentication = authenticationManager.authenticate(token);



            // 2. Role direct authentication object se nikal lijiye
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        System.out.println(role);

            // 3. Authenticated principal se UserDetails nikala
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            System.out.println(userDetails);

            // IF AUTHENTICATION SUCCESS THEN WE HAVE INFO
//        SecurityContextHolder.getContext().setAuthentication(authentication);



            String jwtToken = jwtService.generateToken(userDetails);
            return jwtToken;


        } catch (AuthenticationException e) {
//            e.printStackTrace();
//            return "Could not authenticate: " + e.getMessage();
            throw new BadCredentialsException("Invalid username or password");

        }

//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );


    }
}
