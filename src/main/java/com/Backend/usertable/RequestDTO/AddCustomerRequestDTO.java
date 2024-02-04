package com.Backend.usertable.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerRequestDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String street;
    private String email;
    private String phone;
}
