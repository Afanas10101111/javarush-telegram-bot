package com.github.afanas10101111.jtb.command;

class UserNotFoundExceptionCommandTest extends AbstractCommandTest {
    private static final String FAKE_COMMAND_NAME = "/anyStringCanBeHere";

    @Override
    String getCommandName() {
        return FAKE_COMMAND_NAME;
    }

    @Override
    String getCommandMessage() {
        return UserNotFoundExceptionCommand.MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UserNotFoundExceptionCommand(messageServiceMock);
    }
}
