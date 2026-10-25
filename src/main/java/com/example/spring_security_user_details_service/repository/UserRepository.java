package com.example.spring_security_user_details_service.repository;

import com.example.spring_security_user_details_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByUsername(String username);
}
