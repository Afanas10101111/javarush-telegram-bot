package com.github.afanas10101111.jtb.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandName {
    GREETING("/greeting"),
    START("/start"),
    STOP("/stop"),
    SUBSCRIBE("/subscribe"),
    SUBSCRIPTIONS("/subscriptions"),
    UNSUBSCRIBE("/unsubscribe"),
    KEYBOARD("/keyboard"),
    HELP("/help"),
    ADMIN_HELP("/aHelp"),
    STAT("/stat"),
    NOTIFY("/notify");

    private final String name;
}
