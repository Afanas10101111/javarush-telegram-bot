package com.github.afanas10101111.jtb.command.util;

import com.github.afanas10101111.jtb.command.CommandName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

import static com.github.afanas10101111.jtb.command.CommandName.HELP;
import static com.github.afanas10101111.jtb.command.CommandName.KEYBOARD;
import static com.github.afanas10101111.jtb.command.CommandName.START;
import static com.github.afanas10101111.jtb.command.CommandName.STOP;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIPTIONS;
import static com.github.afanas10101111.jtb.command.CommandName.UNSUBSCRIBE;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardUtilTest {

    @Test
    void shouldReturnCorrectKeyboard() {
        List<List<InlineKeyboardButton>> rowList = KeyboardUtil.getMenuKeyboard().getKeyboard();
        checkRowContainsCommands(rowList.get(0), START, STOP);
        checkRowContainsCommands(rowList.get(1), SUBSCRIBE, UNSUBSCRIBE);
        checkRowContainsCommands(rowList.get(2), SUBSCRIPTIONS);
        checkRowContainsCommands(rowList.get(3), KEYBOARD, HELP);
    }

    private void checkRowContainsCommands(List<InlineKeyboardButton> row, CommandName... commandNames) {
        Arrays.stream(commandNames).forEach(cn -> assertTrue(row.contains(new InlineKeyboardButton(
                cn.toString(), null, cn.getName(), null, null, null, null, null
        ))));
    }
}
