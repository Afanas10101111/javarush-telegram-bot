package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.command.util.KeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static com.github.afanas10101111.jtb.command.CommandName.KEYBOARD;

class KeyboardCommandTest extends AbstractCommandWithKeyboardTest {

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
        return new KeyboardCommand(messageServiceMock);
    }

    @Override
    InlineKeyboardMarkup getKeyboard() {
        return KeyboardUtil.getMenuKeyboard();
    }
}
