package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.command.util.KeyboardUtil;

import static com.github.afanas10101111.jtb.command.CommandName.KEYBOARD;

class KeyboardCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return KEYBOARD.getName();
    }

    @Override
    String getCommandMessage() {
        return KeyboardCommand.MESSAGE;
    }

    @Override
    Command getCommand() {
        return new KeyboardCommand(messageService);
    }

    @Override
    void shouldProperlyExecuteCommand() {
        getCommand().execute(getMockedUpdate());
        verifyMessageServiceCallWithKeyboard(CHAT_ID.toString(), getCommandMessage(), KeyboardUtil.getMenuKeyboard());
    }

    @Override
    void shouldProperlyExecuteCommandFromCallback() {
        getCommand().execute(getMockedUpdateWithCallback());
        verifyMessageServiceCallWithKeyboard(CHAT_ID.toString(), getCommandMessage(), KeyboardUtil.getMenuKeyboard());
    }
}
