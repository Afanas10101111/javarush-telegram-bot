package com.github.afanas10101111.jtb.command.util;

import com.github.afanas10101111.jtb.command.CommandName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.afanas10101111.jtb.command.CommandName.HELP;
import static com.github.afanas10101111.jtb.command.CommandName.KEYBOARD;
import static com.github.afanas10101111.jtb.command.CommandName.START;
import static com.github.afanas10101111.jtb.command.CommandName.STOP;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIPTIONS;
import static com.github.afanas10101111.jtb.command.CommandName.UNSUBSCRIBE;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardUtilTest {
    private static final int NEW_ROW_SIZE = 1;

    @Test
    void shouldReturnCorrectMenuKeyboard() {
        List<List<InlineKeyboardButton>> rowList = KeyboardUtil.getMenuKeyboard().getKeyboard();
        checkRowContainsCommands(rowList.get(0), START, STOP);
        checkRowContainsCommands(rowList.get(1), SUBSCRIBE, UNSUBSCRIBE);
        checkRowContainsCommands(rowList.get(2), SUBSCRIPTIONS);
        checkRowContainsCommands(rowList.get(3), KEYBOARD, HELP);
    }

    @Test
    void shouldReturnCorrectNumericKeyboard() {
        String prefix = "prefix";
        List<Integer> numbers = getNumbers();
        List<List<InlineKeyboardButton>> rowList = KeyboardUtil.getNumericKeyboard(prefix, List.copyOf(numbers)).getKeyboard();
        assertThat(rowList).asList().hasSize(2);
        assertThat(rowList.get(0)).asList().hasSize(KeyboardUtil.MAX_NUMERIC_ROW_SIZE);
        assertThat(rowList.get(1)).asList().hasSize(NEW_ROW_SIZE);
        assertThat(rowList.get(0).get(0).getText()).isEqualTo(numbers.get(0).toString());
        assertThat(rowList.get(0).get(0).getCallbackData()).isEqualTo(prefix + SPACE + numbers.get(0));
    }

    private void checkRowContainsCommands(List<InlineKeyboardButton> row, CommandName... commandNames) {
        Arrays.stream(commandNames).forEach(cn -> assertTrue(row.contains(new InlineKeyboardButton(
                cn.toString(), null, cn.getName(), null, null, null, null, null, null
        ))));
    }

    private List<Integer> getNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < KeyboardUtil.MAX_NUMERIC_ROW_SIZE + NEW_ROW_SIZE; i++) {
            numbers.add(i);
        }
        return numbers;
    }
}
