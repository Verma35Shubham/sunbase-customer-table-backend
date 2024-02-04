//package com.Backend.usertable.Service.ServiceImpl;
//
//import com.Backend.usertable.Models.User;
//import com.Backend.usertable.Repository.UserRepository;
//import com.Backend.usertable.RequestDTO.LoginRequestDTO;
//import com.Backend.usertable.RequestDTO.SignupRequestDTO;
//import com.Backend.usertable.ResponseDTO.AuthResponseDTO;
//import com.Backend.usertable.Service.UserService;
//import com.Backend.usertable.config.JWTProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JWTProvider jwtProvider;
//
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//    @Override
//    public AuthResponseDTO SignupUser(SignupRequestDTO signupRequestDTO) {
//        User user = User.builder()
//                .firstName(signupRequestDTO.getFirstName())
//                .lastName(signupRequestDTO.getLastName())
//                .email(signupRequestDTO.getEmail())
//                .password(signupRequestDTO.getPassword())
//                .role("User")
//                .build();
//        userRepository.save(user);
//        String jwt = jwtProvider.createJwtToken(user);
//
//        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
//        authResponseDTO.setMessage("User registered successful.");
//        authResponseDTO.setJwt(jwt);
//
//        return authResponseDTO;
//    }
//
//    @Override
//    public AuthResponseDTO LoginUser(LoginRequestDTO loginRequestDTO) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
//                loginRequestDTO.getPassword()));
//        User user  = userRepository.findByEmail(loginRequestDTO.getUsername()).orElseThrow();
//        String jwtToken = jwtProvider.createJwtToken(user);
//        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
//        authResponseDTO.setMessage("Welcome Back.");
//        authResponseDTO.setJwt(jwtToken);
//
//        return authResponseDTO;
//    }
//}
