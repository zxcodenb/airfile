package com.zx.fileairdrop.enums;

import lombok.Getter;

@Getter
public enum TimeUnitEnum {
    MINUTE(60),
    HOUR(60 * 60),
    DAY(24 * 60 * 60),
    WEEK(7 * 24 * 60 * 60),
    DECADE(10 * 365 * 24 * 60 * 60);


    private final int seconds;

    TimeUnitEnum(int seconds) {
        this.seconds = seconds;
    }

}
