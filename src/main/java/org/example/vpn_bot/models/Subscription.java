package org.example.vpn_bot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String status;
    private int dataLimit;
    private int dataUsed;
    private LocalDate expiryDate;

    // Геттеры и сеттеры
}