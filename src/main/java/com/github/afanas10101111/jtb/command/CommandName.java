package com.github.afanas10101111.jtb.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandName {
    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    STAT("/stat"),
    SUBSCRIBE("/subscribe"),
    VIEW_SUBSCRIPTIONS("/viewSubscriptions"),
    UNSUBSCRIBE("/unsubscribe"),
    ADMIN_HELP("/aHelp");

    private final String name;
}
