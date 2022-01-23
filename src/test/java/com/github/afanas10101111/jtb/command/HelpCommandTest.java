package com.github.afanas10101111.jtb.command;

import static com.github.afanas10101111.jtb.command.CommandName.HELP;

class HelpCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return HELP.getName();
    }

    @Override
    String getCommandMessage() {
        return HelpCommand.MESSAGE;
    }

    @Override
    Command getCommand() {
        return new HelpCommand(service);
    }
}
