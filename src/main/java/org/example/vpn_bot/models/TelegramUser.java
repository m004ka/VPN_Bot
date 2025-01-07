package org.example.vpn_bot.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.vpn_bot.repositories.TelegramUserRepository;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;


@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TelegramUser {
    private Long userId;
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;

    private String link;

    private Timestamp registeredAt;




}
