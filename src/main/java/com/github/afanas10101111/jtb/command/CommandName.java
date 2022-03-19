package com.github.afanas10101111.jtb.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandName {
    GREETING("/greeting"),
    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    SUBSCRIBE("/subscribe"),
    VIEW_SUBSCRIPTIONS("/viewSubscriptions"),
    UNSUBSCRIBE("/unsubscribe"),
    ADMIN_HELP("/aHelp"),
    STAT("/stat"),
    NOTIFY("/notify");

    private final String name;
}
