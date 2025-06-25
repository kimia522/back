package com.co2fp.backend.backend.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    public String username;
    public String password;
    public String firstname;
    public String lastname;
    public String email;
    public boolean admin;
}
