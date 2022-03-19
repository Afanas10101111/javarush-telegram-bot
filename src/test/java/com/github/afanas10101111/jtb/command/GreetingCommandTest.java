package com.github.afanas10101111.jtb.command;

import static com.github.afanas10101111.jtb.command.CommandName.GREETING;

class GreetingCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return GREETING.getName();
    }

    @Override
    String getCommandMessage() {
        return GreetingCommand.MESSAGE;
    }

    @Override
    Command getCommand() {
        return new GreetingCommand(messageService);
    }
}
