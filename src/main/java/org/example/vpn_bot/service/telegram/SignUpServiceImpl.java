package org.example.vpn_bot.service.telegram;


import lombok.RequiredArgsConstructor;

import org.example.vpn_bot.models.TelegramUser;
import org.example.vpn_bot.repositories.TelegramUserRepository;
import org.example.vpn_bot.service.interfaces.SignUpService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final TelegramUserRepository tgUserRepo;


    @Override
    public void SignUp(Update update) {
        String link = null;

        if (update.getMessage().getFrom().getUserName() != null) {
            link = "https://t.me/" + update.getMessage().getFrom().getUserName(); // Формируем ссылку
        }
        var message = update.getMessage();
        var contact = update.getMessage().getContact();
        TelegramUser telegramUser = TelegramUser.builder()
                .chatId(update.getMessage().getChatId())
                .registeredAt(Timestamp.from(Instant.now()))
                .username(update.getMessage().getFrom().getUserName())
                .firstName(update.getMessage().getFrom().getFirstName())
                .lastName(update.getMessage().getFrom().getLastName())
                .userId(update.getMessage().getFrom().getId())
                .link(link)
                .build();
        tgUserRepo.save(telegramUser);
    }
}
