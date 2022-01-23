package com.github.afanas10101111.jtb.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandName {
    START("/start"),
    STOP("/stop"),
    HELP("/help");

    private final String name;
}
