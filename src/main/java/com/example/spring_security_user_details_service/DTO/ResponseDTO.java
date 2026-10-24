package com.example.spring_security_user_details_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private String id;
    private String message;
    private String fullName;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
