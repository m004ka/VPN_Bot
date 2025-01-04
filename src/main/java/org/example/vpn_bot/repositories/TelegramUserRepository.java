package org.example.vpn_bot.repositories;

import org.example.vpn_bot.models.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    Boolean existsTelegramUserByChatId(Long id);
}
