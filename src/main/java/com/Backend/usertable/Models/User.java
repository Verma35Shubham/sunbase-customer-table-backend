//package com.Backend.usertable.Models;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Builder
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    private String firstName;
//    private String lastName;
//    @Column(unique = true, nullable = false)
//    private String email;
//    @Column(nullable = false)
//    private String password;
//    @Column(unique = true)
//    private String role;
//}
