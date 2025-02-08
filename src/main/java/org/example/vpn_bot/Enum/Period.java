package org.example.vpn_bot.Enum;

import lombok.Getter;

@Getter
public enum Period {
    MONTH(1),
    THREE_MONTH(3),
    SIX_MONTH(6),
    YEAR(12);

    private final int months;

    Period(int months){
        this.months = months;
    }

}
