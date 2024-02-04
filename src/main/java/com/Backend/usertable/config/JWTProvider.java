package com.Backend.usertable.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTProvider {

//    for generating token first set the claims which is in Map<String, Object>, here we use SignatureAlgorithm.HS512
//    and JWTConstant.SECRET_KEY and also we set the Issued time and expiration and compact the token
    private String generateToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24)).compact();
    }
    // for new token call this method using UserDetails
    public String createJwtToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, userDetails.getUsername());
    }

    private <T> T getClaimFromJwt(String jwt, Function<Claims,T> claimsTFunction){
        final Claims claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(jwt).getBody();
        return claimsTFunction.apply(claims);
    }
    private Key getSecretKey(){
        byte[] bytes = Decoders.BASE64.decode(JWTConstant.SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    // get username(email) using this method
    public String getUserNameFromJwt(String jwt){
        return getClaimFromJwt(jwt, Claims::getSubject);
    }

    // for jwt validation use this method
    public Boolean isTokenExist(String jwt, UserDetails userDetails){
        final String username = getUserNameFromJwt(jwt);
        return (username.equals(userDetails.getUsername()) && !isTokenValid(jwt));
    }
    public Date getExpirationDateFromToken(String jwt){
        return getClaimFromJwt(jwt, Claims::getExpiration);
    }

    // call this method check jwt expiration
    private Boolean isTokenValid(String jwt){
        final Date expiredDate = getExpirationDateFromToken(jwt);
        return expiredDate.before(new Date());
    }

}
