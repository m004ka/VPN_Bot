package org.example.vpn_bot.service.config;

import lombok.extern.slf4j.Slf4j;
import org.example.vpn_bot.panel_x_ui.HttpsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class PropertyUpdateService {

    private final ConfigurableEnvironment environment;
    private final HttpsService httpsService; // Убедитесь, что этот сервис корректно внедрен

    public PropertyUpdateService(ConfigurableEnvironment environment, HttpsService httpsService) {
        this.environment = environment;
        this.httpsService = httpsService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Каждый день в полночь
    public void updateProperty() {
        try {
            log.info("Запуск обновления свойства panel.session...");

            // Запрос нового значения
            String newValue = fetchNewValueFromApi();
            log.info("Получено новое значение для panel.session: {}", newValue);

            // Обновляем свойство в Environment
            MutablePropertySources propertySources = environment.getPropertySources();
            Properties properties = new Properties();
            properties.put("panel.session", newValue);
            propertySources.addFirst(new PropertiesPropertySource("dynamicProperties", properties));

            log.info("Свойство panel.session успешно обновлено.");
        } catch (Exception e) {
            log.error("Ошибка при обновлении свойства panel.session: ", e);
        }
    }

    private String fetchNewValueFromApi() {
        log.info("Отправка запроса на получение нового значения panel.session...");
        List<String> cook = httpsService.SendPostAuthorization();

        if (cook == null || cook.isEmpty()) {
            log.warn("Ответ от SendPostAuthorization пустой или null!");
            return "defaultSessionValue"; // Подставьте значение по умолчанию, если запрос не удался
        }

        log.info("Успешно получено новое значение panel.session из API.");
        System.out.println("Кука  "  + extractCookie(cook.get(0)));
        return extractCookie(cook.get(0));
    }

    public static String extractCookie(String cookieString) {
        if (cookieString == null || cookieString.isEmpty()) {
            return null;
        }

        // Регулярное выражение для поиска куки "3x-ui=" и её значения
        Pattern pattern = Pattern.compile("(3x-ui=[^;]+)");
        Matcher matcher = pattern.matcher(cookieString);

        if (matcher.find()) {
            return matcher.group(1); // Возвращаем полное значение куки
        } else {
            return null; // Если кука не найдена, возвращаем null
        }
    }
}
