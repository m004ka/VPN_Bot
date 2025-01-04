package org.example.vpn_bot.panel_x_ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.vpn_bot.models.TelegramUser;
import org.example.vpn_bot.repositories.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendTestConfigToClient {

    @Value("${panel.addClient}")
    private String url;

    private final TelegramUserRepository telegramUserRepository;

    private final ObjectMapper objectMapper;

    public void addClientToInbound(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        TelegramUser telegramUser = telegramUserRepository
                .findByChatId(chatId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Telegram user with chat ID " + chatId + " not found."));

        // Генерация даты в формате Unix timestamp на 14 дней позже текущего времени
        long expiryTimestampMillis = Instant.now().plus(14, ChronoUnit.DAYS).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

        // Используем LinkedHashMap для соблюдения порядка ключей
        Map<String, Object> clientData = new LinkedHashMap<>();
        clientData.put("id", String.valueOf(telegramUser.getChatId())); // ID в кавычках
        clientData.put("flow", "");
        clientData.put("email", telegramUser.getLink());
        clientData.put("limitIp", 0);
        clientData.put("totalGB", 0);
        clientData.put("expiryTime", expiryTimestampMillis); // Устанавливаем сгенерированный timestamp в миллисекундах
        clientData.put("enable", true);
        clientData.put("tgId", telegramUser.getUserId());
        clientData.put("subId", telegramUser.getUsername());
        clientData.put("reset", 0);

        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("clients", Collections.singletonList(clientData));

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("id", 1);  // ID в начале
        try {
            String settingsJson = objectMapper.writeValueAsString(settings);
            requestBody.put("settings", settingsJson); // Сериализуем данные

            // Выводим JSON, который будет отправлен
            System.out.println("Отправляемый JSON: " + objectMapper.writeValueAsString(requestBody));

        } catch (Exception e) {
            log.error("Ошибка при сериализации данных клиента в JSON: ", e);
            throw new RuntimeException("Ошибка при подготовке запроса", e);
        }

        // Создание заголовков
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Добавление куки с session
        String sessionId = "3x-ui=MTczNTk2MTY0NXxEWDhFQVFMX2dBQUJFQUVRQUFCMV80QUFBUVp6ZEhKcGJtY01EQUFLVEU5SFNVNWZWVk5GVWhoNExYVnBMMlJoZEdGaVlYTmxMMjF2WkdWc0xsVnpaWExfZ1FNQkFRUlZjMlZ5QWYtQ0FBRUVBUUpKWkFFRUFBRUlWWE5sY201aGJXVUJEQUFCQ0ZCaGMzTjNiM0prQVF3QUFRdE1iMmRwYmxObFkzSmxkQUVNQUFBQUh2LUNHd0VDQVFwRFIwdFVWbTFrVTNOdUFRbzRaM1ZXUTJVNU9YTnFBQT09fNtxU4yDc6HsMB9s7o15nCkHM4e9qjItPCO7HWHk54jI"; // Подставьте сюда свой session ID
        headers.add("Cookie",  sessionId);


        // Создание объекта запроса
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Создание RestTemplate для выполнения запроса
        RestTemplate restTemplate = new RestTemplate();

        // Отправка запроса и обработка ответа
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Ответ от сервера: " + response.getBody());
            } else {
                System.out.println("Ошибка при отправке запроса. Статус: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Ошибка при отправке конфигурации для пользователя (chatId: " + chatId + "): " + e.getMessage(), e);
        }
    }
}
