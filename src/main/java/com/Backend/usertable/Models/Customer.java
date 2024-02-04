package com.Backend.usertable.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String uuid;
    private String firstname;
    private String lastname;
    private String address;
    private String city;
    private String street;
    private String state;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;

}
