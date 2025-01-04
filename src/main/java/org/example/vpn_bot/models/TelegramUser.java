package org.example.vpn_bot.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TelegramUser {
    @Id
    private Long chatId;

    private String firstName;
    private String lastName;
    private String username;

    private String link;

    private Timestamp registeredAt;

//    public Long getChatId() {
//        return chatId;
//    }
//
//    public void setChatId(Long chatId) {
//        this.chatId = chatId;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getLink() {
//        return link;
//    }
//
//    public void setLink(String link) {
//        this.link = link;
//    }
//
//    public Timestamp getRegisteredAt() {
//        return registeredAt;
//    }
//
//    public void setRegisteredAt(Timestamp registeredAt) {
//        this.registeredAt = registeredAt;
//    }
}
