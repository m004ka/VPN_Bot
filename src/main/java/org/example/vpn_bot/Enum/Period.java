package org.example.vpn_bot.Enum;

import lombok.Getter;

@Getter
public enum Period {
    MONTH(1, 199),
    THREE_MONTH(3, 499),
    SIX_MONTH(6, 799),
    YEAR(12, 1499);

    private final int months;
    private final double money;

    Period(int months, double money){
        this.months = months;
        this.money = money;

    }
}
