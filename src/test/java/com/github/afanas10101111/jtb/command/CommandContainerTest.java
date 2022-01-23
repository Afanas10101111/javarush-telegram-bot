package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

class CommandContainerTest {
    private static final String UNKNOWN_COMMAND = "asdf";

    private final CommandContainer container = new CommandContainer(Mockito.mock(SendBotMessageService.class));

    @Test
    void shouldGetAllTheExistingCommands() {
        Arrays.stream(CommandName.values())
                .forEach(name -> {
                    Command command = container.retrieveCommand(name.getName());
                    Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
                });
    }

    @Test
    void shouldReturnUnknownCommand() {
        Command command = container.retrieveCommand(UNKNOWN_COMMAND);
        Assertions.assertEquals(UnknownCommand.class, command.getClass());
    }
}
