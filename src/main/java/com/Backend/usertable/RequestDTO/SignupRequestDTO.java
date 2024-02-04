package com.Backend.usertable.RequestDTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
