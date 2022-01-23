package com.github.afanas10101111.jtb.command;

import static com.github.afanas10101111.jtb.command.CommandName.STOP;

class StopCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return STOP.getName();
    }

    @Override
    String getCommandMessage() {
        return StopCommand.MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StopCommand(service);
    }
}
