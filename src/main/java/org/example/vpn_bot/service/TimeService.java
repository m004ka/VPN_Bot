package org.example.vpn_bot.service;

import lombok.RequiredArgsConstructor;
import org.example.vpn_bot.panel_x_ui.ApiOptions;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final ApiOptions apiOptions;

    private List<Long> getTime(long lastTime) {
        long currentTime = Instant.now().toEpochMilli();
        long diff = lastTime - currentTime;

        // Переводим разницу в секунды
        long seconds = diff / 1000;

        // Вычисляем дни, часы, минуты и секунды
        long days = seconds / (24 * 60 * 60);
        seconds %= (24 * 60 * 60); // Остаток секунд после дней

        long hours = seconds / (60 * 60);
        seconds %= (60 * 60); // Остаток секунд после часов

        long minutes = seconds / 60;
        seconds %= 60; // Остаток секунд после минут
        List<Long> arr = new ArrayList<>();
        arr.add(days);
        arr.add(hours);
        arr.add(minutes);
        arr.add(seconds);
        System.out.println("тут были" + arr);
        return arr;


    }

    private List<Long> getLostTime() {
        // Вычисляем дни, часы, минуты и секунды
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        List<Long> arr = new ArrayList<>();
        arr.add(days);
        arr.add(hours);
        arr.add(minutes);
        arr.add(seconds);
        System.out.println("тут были" + arr);
        return arr;
    }


    public List<Long> checkTimeAndExecute(long id) {
        long lastTime = apiOptions.getTimeToLeft(id);
        // Получаем текущее время в миллисекундах
        long currentTime = Instant.now().toEpochMilli();

        // Сравниваем текущее время с переданным timestamp
        if (lastTime < currentTime) {
            return getLostTime();
        } else {
            return getTime(lastTime);
        }
    }
}
