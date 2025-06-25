package com.co2fp.backend.backend.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long user_id;
    private String email;
    private String username;
    private String firstname;
    private String lastname;
    private boolean admin;
}
