package com.example.spring_security_user_details_service.SecurityConfig;

import com.example.spring_security_user_details_service.models.User;
import com.example.spring_security_user_details_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Configuration
@Service
//@AllArgsConstructor // yh automatic construtor kr dega
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username);
       if (user == null) {
           throw new UsernameNotFoundException("User not found with username: " + username);
       }

       UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
               .username(user.getUsername())
               .password(user.getPassword())
//               .authorities(String.valueOf(user.getRole()))
               .roles(String.valueOf(user.getRole()))
               .build();

       return userDetails;

   }
}
