package com.Backend.usertable.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CustomerResponseDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;
    private String street;
}
