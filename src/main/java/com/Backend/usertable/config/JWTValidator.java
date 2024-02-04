package com.Backend.usertable.config;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
@RequiredArgsConstructor
public class JWTValidator extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTProvider jwtProvider;

    private Logger logger = LoggerFactory.getLogger(JWTValidator.class);

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        logRequestHeader(request);
        String requestHeader = request.getHeader(JWTConstant.JWT_HEADER);
        logger.info("Header: {}", requestHeader);
        String userName = null;
        String jwt = null;

        if(requestHeader != null && requestHeader.startsWith("Bearer")){
            jwt = requestHeader.substring(7);
            try{
                userName = this.jwtProvider.getUserNameFromJwt(jwt);
            }catch (ExpiredJwtException e){
                logger.error("This jwt token expired!!", e);
            }catch (IllegalArgumentException e){
                logger.error("While fetching username, there is IllegalArgumentException!!", e);
            }catch (MalformedJwtException e){
                logger.error("Invalid Jwt token!!", e);
            }catch (Exception e){
                logger.error("Unexpected error occured !!", e);
            }
        }else{
            logger.info("Validation fails !!");
        }
        if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            // before setting authentication check jwt token validation.
            Boolean validateJwt = this.jwtProvider.isTokenExist(jwt, userDetails);

            if(validateJwt){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                logger.info("Jwt token not valid!!");
            }
        }
        filterChain.doFilter(request,response);
    }
    private void logRequestHeader(HttpServletRequest httpServletRequest){
        Enumeration<String> requestHeaderNames = httpServletRequest.getHeaderNames();
        while(requestHeaderNames.hasMoreElements()){
            String headerName = requestHeaderNames.nextElement();
            String headerValue = httpServletRequest.getHeader(headerName);
            logger.info("Request Header - {}: {}",headerName, headerValue);
        }
    }
}
