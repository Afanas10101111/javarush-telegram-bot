package com.github.afanas10101111.jtb.command;

import static com.github.afanas10101111.jtb.command.CommandName.START;

class StartCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return START.getName();
    }

    @Override
    String getCommandMessage() {
        return StartCommand.MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StartCommand(messageService, userService);
    }
}
