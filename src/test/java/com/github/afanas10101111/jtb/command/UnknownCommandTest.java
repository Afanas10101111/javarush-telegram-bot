package com.github.afanas10101111.jtb.command;

class UnknownCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return "/asdf";
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
