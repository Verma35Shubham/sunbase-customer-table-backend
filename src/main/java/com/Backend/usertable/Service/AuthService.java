package com.Backend.usertable.Service;

import java.util.List;
import java.util.Map;

public interface AuthService {
    public String authenticateJwtToken();
    public List<Map<String, Object>> fetchCustmerData();
}
