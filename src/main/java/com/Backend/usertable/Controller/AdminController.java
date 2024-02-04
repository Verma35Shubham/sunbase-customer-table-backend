package com.Backend.usertable.Controller;

import com.Backend.usertable.RequestDTO.LoginRequestDTO;
import com.Backend.usertable.ResponseDTO.LoginResponseDTO;
import com.Backend.usertable.config.JWTProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/sunbase/portal/api")
public class AdminController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTProvider jwtProvider;

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        this.validateAuthenticate(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getUsername());
        String jwt = this.jwtProvider.createJwtToken(userDetails);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setJwt(jwt);
        loginResponseDTO.setUsername(userDetails.getUsername());

        return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
    }

    private void validateAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try{
            authenticationManager.authenticate(passwordAuthenticationToken);
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Please check your input. Invalid Username or Password!!");
        }
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> exceptionHandler(){
        return new ResponseEntity("Please check your input. Invalid Username or Password!!", HttpStatus.BAD_REQUEST);
    }
}
