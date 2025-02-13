package org.example.vpn_bot.YooKassa;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.vpn_bot.Enum.Period;
import org.example.vpn_bot.models.yooKassa.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final RestClient yooKassaClient;

    @SneakyThrows
    public PaymentResponse createPayment(double amount, String description, String userEmail) {
        var request = PaymentRequest.create(amount, description, userEmail);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);
        System.out.println("Отправляемый JSON: " + requestBody);

        return yooKassaClient.post().body(request).retrieve().body(PaymentResponse.class);
    }


    public String getUrlPayment(Update update, Period period, boolean check, String mailUser) {
        PaymentResponse url;
        if (check) {
            String description = "Заказ пользователя: " + update.getCallbackQuery().getFrom().getUserName();
           url = createPayment(period.getMoney(), description, mailUser);
        } else {
            String description = "Заказ пользователя: " + update.getCallbackQuery().getFrom().getUserName();
           url = createPayment(period.getMoney(), description, "ysupov959@gmail.com");
        }

        return url.getConfirmation().getConfirmation_url();
    }


}
