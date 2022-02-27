package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Set;

class CommandContainerTest {
    private static final String UNKNOWN_COMMAND = "needMoreGold";
    private static final String ADMIN = "admin";
    private static final String USER = "user";

    private final CommandContainer container = new CommandContainer(
            Mockito.mock(SendBotMessageService.class),
            Mockito.mock(UserService.class),
            Mockito.mock(GroupSubService.class),
            Mockito.mock(GroupClient.class),
            Set.of(ADMIN)
    );

    @Test
    void shouldGetAllTheExistingCommands() {
        Arrays.stream(CommandName.values())
                .forEach(name -> {
                    Command command = container.retrieveCommand(name.getName(), ADMIN);
                    Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
                });
    }

    @Test
    void shouldReturnUnknownCommand() {
        Command command = container.retrieveCommand(UNKNOWN_COMMAND, ADMIN);
        Assertions.assertEquals(UnknownCommand.class, command.getClass());
    }

    @Test
    void shouldReturnUnknownCommandToUserOnAdminRequest() {
        Command stat = container.retrieveCommand(CommandName.STAT.getName(), USER);
        Assertions.assertEquals(UnknownCommand.class, stat.getClass());
        Command aHelp = container.retrieveCommand(CommandName.ADMIN_HELP.getName(), USER);
        Assertions.assertEquals(UnknownCommand.class, aHelp.getClass());
    }
}
