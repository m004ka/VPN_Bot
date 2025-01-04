package org.example.vpn_bot.panel_x_ui;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HttpsService {
    @Value("${panel.url}")
    String url;
    @Value("${panel.login}")
    String login;
    @Value("${panel.password}")
    String password;

    private final RestTemplate restTemplate;

    public HttpsService() {
        this.restTemplate = new RestTemplate();
    }

    public String SendPostAuthorization() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Формируем тело запроса (x-www-form-urlencoded)

        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String body = "username=" + login + "&password=" + password;

        // Создаем HttpEntity с телом запроса и заголовками
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Отправка POST запроса
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Извлекаем куки из заголовков ответа
        List<String> cookies = response.getHeaders().get("Set-Cookie");

        // Если есть куки, можно извлечь их
        if (cookies != null) {
            for (String cookie : cookies) {
                System.out.println("Cookie: " + cookie);
            }
        }

        // Возвращаем ответ в теле
        return response.getBody();
    }
}