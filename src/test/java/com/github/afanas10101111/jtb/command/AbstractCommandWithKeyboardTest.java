package com.github.afanas10101111.jtb.command;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

abstract class AbstractCommandWithKeyboardTest extends AbstractCommandTest  {
    abstract InlineKeyboardMarkup getKeyboard();

    @Override
    @Test
    void shouldProperlyExecuteCommand() {
        getCommand().execute(getMockedUpdate());
        verifyMessageServiceCallWithKeyboard(CHAT_ID.toString(), getCommandMessage(), getKeyboard());
    }

    @Override
    @Test
    void shouldProperlyExecuteCommandFromCallback() {
        getCommand().execute(getMockedUpdateWithCallback());
        verifyMessageServiceCallWithKeyboard(CHAT_ID.toString(), getCommandMessage(), getKeyboard());
    }
}
