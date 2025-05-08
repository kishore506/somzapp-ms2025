package com.somz.dev.OrdrSomz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ordr")
public class OrdrController {

    @Autowired
    private RestTemplate restTemplate;

    // Endpoint to fetch JWT token from backend
    @PostMapping("/get-token")
    public ResponseEntity<String> fetchJwtToken() {
        String loginUrl = "http://localhost:8085/api/v1/auth/login";

        Map<String, String> request = new HashMap<>();
        request.put("username", "admin");
        request.put("password", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(loginUrl, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to fetch JWT token: " + ex.getMessage());
        }
    }

    // Endpoint to fetch orders from backend using the JWT token
    @GetMapping("/fetch-orders")
    public ResponseEntity<String> getOrders() {
        ResponseEntity<String> tokenResponse = fetchJwtToken();

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized - Token fetch failed");
        }

        String token = tokenResponse.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String ordersApiUrl = "http://localhost:8085/api/v1/orders";  // Make sure this backend endpoint exists

        try {
            ResponseEntity<String> ordersResponse = restTemplate.exchange(
                    ordersApiUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return ResponseEntity.status(ordersResponse.getStatusCode()).body(ordersResponse.getBody());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to fetch orders: " + ex.getMessage());
        }
    }
}
