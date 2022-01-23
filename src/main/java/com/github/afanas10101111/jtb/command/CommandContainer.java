package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static com.github.afanas10101111.jtb.command.CommandName.HELP;
import static com.github.afanas10101111.jtb.command.CommandName.START;
import static com.github.afanas10101111.jtb.command.CommandName.STOP;

public class CommandContainer {
    private final Map<String, Command> commands;
    private final Command unknown;

    public CommandContainer(SendBotMessageService service) {
        unknown = new UnknownCommand(service);
        commands = ImmutableMap.<String, Command>builder()
                .put(START.getName(), new StartCommand(service))
                .put(STOP.getName(), new StopCommand(service))
                .put(HELP.getName(), new HelpCommand(service))
                .build();
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commands.getOrDefault(commandIdentifier, unknown);
    }
}
