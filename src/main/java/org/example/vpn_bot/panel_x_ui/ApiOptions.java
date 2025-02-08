package org.example.vpn_bot.panel_x_ui;

import com.fasterxml.jackson.databind.JsonNode;
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
public class ApiOptions {

    @Value("${panel.addClient}")
    private String url;

    @Value("${panel.session}")
    String sessionId;

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
        if(telegramUser.getUsername() == null){
            telegramUser.setUsername("id" + telegramUser.getChatId());
            telegramUserRepository.save(telegramUser);
        }
        // Используем LinkedHashMap для соблюдения порядка ключей
        Map<String, Object> clientData = new LinkedHashMap<>();
        clientData.put("id", String.valueOf(telegramUser.getChatId())); // ID в кавычках
        clientData.put("flow", "xtls-rprx-vision");
        clientData.put("email", telegramUser.getUsername());
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
        requestBody.put("id", 6);  // ID в начале
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


         headers.add("Cookie", sessionId);


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

    public Long getTimeToLeft(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", sessionId);
        String urlId = "https://develop-m004ka.ru:7333/lQ4Sfx2VaytIW0c/panel/api/inbounds/getClientTrafficsById/" + id;
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // Создаём RestTemplate для выполнения запроса
        RestTemplate restTemplate = new RestTemplate();

        // Отправка GET-запроса
        try {
            ResponseEntity<String> response = restTemplate.exchange(urlId, HttpMethod.GET, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                try {
                    // Создаем ObjectMapper для парсинга JSON
                    ObjectMapper objectMapper = new ObjectMapper();

                    // Преобразуем строку JSON в JsonNode
                    JsonNode rootNode = objectMapper.readTree(response.getBody());

                    // Получаем массив объектов из поля "obj"
                    JsonNode objArray = rootNode.get("obj");

                    // Проверяем, что массив не пустой
                    if (objArray != null && objArray.size() > 0) {
                        // Извлекаем первый объект из массива
                        JsonNode firstObject = objArray.get(0);

                        // Извлекаем значение поля "expiryTime"
                        Long expiryTime = firstObject.get("expiryTime").asLong();

                        // Выводим значение expiryTime
                        System.out.println("Ошибка при отправке запроса. experyTime Статус: " + expiryTime);
                        return expiryTime;
                    } else {
                        System.out.println("Ошибка при отправке запроса. Пусто Статус: " + response.getStatusCode());
                        log.error("Массив obj пуст.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Ошибка при отправке запроса. Статус: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Ошибка при выполнении GET-запроса для пользователя (chatId: " + id + "): " + e.getMessage(), e);
        }


        return null;
    }

}
