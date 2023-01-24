package com.github.afanas10101111.jtb.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Emoji {
    EXCLAMATION_SIGN("❕"),
    COFFEE_SIGN("☕"),
    GREETING_SIGN("\uD83D\uDC4B"),
    KEYS_SIGN("\uD83C\uDFB9"),
    SMILE("\uD83D\uDE42"),
    UPSIDE_DOWN_SMILE("\uD83D\uDE43"),
    WINKING_SMILE("\uD83D\uDE09"),
    SUNGLASSES_SMILE("\uD83D\uDE0E"),
    TERRIFY_SMILE("\uD83D\uDE31"),
    SAD_SMILE("\uD83D\uDE14"),
    SHIT("\uD83D\uDCA9");

    private final String textValue;
}
