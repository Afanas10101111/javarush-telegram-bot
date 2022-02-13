package com.github.afanas10101111.jtb.command;

import static com.github.afanas10101111.jtb.command.CommandName.STAT;

class StatCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return STAT.getName();
    }

    @Override
    String getCommandMessage() {
        return String.format(StatCommand.MESSAGE, 0);
    }

    @Override
    Command getCommand() {
        return new StatCommand(messageService, userService);
    }
}
