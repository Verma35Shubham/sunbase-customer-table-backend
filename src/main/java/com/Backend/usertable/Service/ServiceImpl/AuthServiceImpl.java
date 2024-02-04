package com.Backend.usertable.Service.ServiceImpl;

import com.Backend.usertable.Service.AuthService;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.google.gson.JsonObject;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RestTemplate restTemplate;
    @Override
    public String authenticateJwtToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type",  "application/json");
        String loginId = "test@sunbasedata.com";
        String password = "Test@123";

        HttpEntity<String> request = new HttpEntity<>(
                "{\"login_id\":\"" + loginId + "\", " +
                        "\"password\":\"" + password + "\"}", httpHeaders);
        String AUTH_URL = "https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                AUTH_URL,request, String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return responseEntity.getBody();
        }else{
            throw new RuntimeException("Jwt authentication failed!!");
        }
    }

    @Override
    public List<Map<String, Object>> fetchCustmerData() {
        String jwt = convertStringToJson(authenticateJwtToken());
        String CUSTOMER_URL = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(jwt);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(CUSTOMER_URL).queryParam("cmd","get_customer_list");
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return responseEntity.getBody();
        }else{
            throw new RuntimeException("authentication failed!!");
        }
    }
    public String convertStringToJson(String jwt){
        JsonObject jsonObject = JsonParser.parseString(jwt).getAsJsonObject();
        return jsonObject.get("access_token").getAsString();
    }
}
