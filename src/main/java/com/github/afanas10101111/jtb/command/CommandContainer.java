package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static com.github.afanas10101111.jtb.command.CommandName.HELP;
import static com.github.afanas10101111.jtb.command.CommandName.START;
import static com.github.afanas10101111.jtb.command.CommandName.STAT;
import static com.github.afanas10101111.jtb.command.CommandName.STOP;

public class CommandContainer {
    private final Map<String, Command> commands;
    private final Command unknown;

    public CommandContainer(SendBotMessageService messageService, UserService userService) {
        unknown = new UnknownCommand(messageService);
        commands = ImmutableMap.<String, Command>builder()
                .put(START.getName(), new StartCommand(messageService, userService))
                .put(STOP.getName(), new StopCommand(messageService, userService))
                .put(HELP.getName(), new HelpCommand(messageService))
                .put(STAT.getName(), new StatCommand(messageService, userService))
                .build();
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commands.getOrDefault(commandIdentifier, unknown);
    }
}
