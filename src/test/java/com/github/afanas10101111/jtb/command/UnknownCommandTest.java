package com.github.afanas10101111.jtb.command;

class UnknownCommandTest extends AbstractCommandTest {
    private static final String UNKNOWN_COMMAND_NAME = "/needMoreGold";

    @Override
    String getCommandName() {
        return UNKNOWN_COMMAND_NAME;
    }

    @Override
    String getCommandMessage() {
        return UnknownCommand.MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UnknownCommand(messageService);
    }
}
