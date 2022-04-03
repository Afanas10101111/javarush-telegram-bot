package com.github.afanas10101111.jtb.command;

import java.util.Set;

class OtherExceptionCommandTest extends AbstractCommandTest {
    private static final String FAKE_COMMAND_NAME = "/anyStringCanBeHere";
    private static final String ADMIN_NAME = "Admin";

    @Override
    String getCommandName() {
        return FAKE_COMMAND_NAME;
    }

    @Override
    String getCommandMessage() {
        return OtherExceptionCommand.MESSAGE + OtherExceptionCommand.TELEGRAM_USER_NAME_PREFIX + ADMIN_NAME;
    }

    @Override
    Command getCommand() {
        return new OtherExceptionCommand(messageService, Set.of(ADMIN_NAME));
    }
}
