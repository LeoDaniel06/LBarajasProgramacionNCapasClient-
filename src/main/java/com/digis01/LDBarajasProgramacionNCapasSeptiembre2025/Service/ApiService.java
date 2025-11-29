
package com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.Service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> ResponseEntity<T> get(String url, Class<T> responseType, HttpSession session) {
        String token = (String) session.getAttribute("token");

        if (token == null) {
            throw new RuntimeException("No hay token en la sesión. El usuario no está autenticado.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    }
}