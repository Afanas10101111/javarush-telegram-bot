package com.github.afanas10101111.jtb.command;

import static com.github.afanas10101111.jtb.command.CommandName.ADMIN_HELP;

class AdminHelpCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return ADMIN_HELP.getName();
    }

    @Override
    String getCommandMessage() {
        return AdminHelpCommand.MESSAGE;
    }

    @Override
    Command getCommand() {
        return new AdminHelpCommand(messageService);
    }
}
