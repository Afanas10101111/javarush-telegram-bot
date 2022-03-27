package com.github.afanas10101111.jtb.command.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.afanas10101111.jtb.command.CommandName.HELP;
import static com.github.afanas10101111.jtb.command.CommandName.KEYBOARD;
import static com.github.afanas10101111.jtb.command.CommandName.START;
import static com.github.afanas10101111.jtb.command.CommandName.STOP;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIPTIONS;
import static com.github.afanas10101111.jtb.command.CommandName.UNSUBSCRIBE;
import static org.apache.commons.lang3.StringUtils.SPACE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyboardUtil {
    public static final int MAX_NUMERIC_ROW_SIZE = 5;

    private static InlineKeyboardMarkup menuKeyboard;

    public static InlineKeyboardMarkup getMenuKeyboard() {
        if (menuKeyboard == null) {
            initializeMenuKeyboard();
        }
        return menuKeyboard;
    }

    public static InlineKeyboardMarkup getNumericKeyboard(String dataPrefix, List<Integer> numbers) {
        return new InlineKeyboardMarkup(getRowListForNumbers(dataPrefix, numbers));
    }

    private static void initializeMenuKeyboard() {
        List<InlineKeyboardButton> firstRow = Arrays.asList(
                createButton(START.toString(), START.getName()),
                createButton(STOP.toString(), STOP.getName())
        );
        List<InlineKeyboardButton> secondRow = Arrays.asList(
                createButton(SUBSCRIBE.toString(), SUBSCRIBE.getName()),
                createButton(UNSUBSCRIBE.toString(), UNSUBSCRIBE.getName())
        );
        List<InlineKeyboardButton> thirdRow = Collections.singletonList(
                createButton(SUBSCRIPTIONS.toString(), SUBSCRIPTIONS.getName())
        );
        List<InlineKeyboardButton> fourthRow = Arrays.asList(
                createButton(KEYBOARD.toString(), KEYBOARD.getName()),
                createButton(HELP.toString(), HELP.getName())
        );
        List<List<InlineKeyboardButton>> rowList = Arrays.asList(firstRow, secondRow, thirdRow, fourthRow);

        menuKeyboard = new InlineKeyboardMarkup(rowList);
    }

    private static List<List<InlineKeyboardButton>> getRowListForNumbers(String dataPrefix, List<Integer> numbers) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (Integer number : numbers) {
            if (isRowFilled(row)) {
                rowList.add(List.copyOf(row));
                row = new ArrayList<>();
            }
            row.add(createButton(number.toString(), dataPrefix + SPACE + number));
        }
        rowList.add(row);
        return rowList;
    }

    private static boolean isRowFilled(List<InlineKeyboardButton> row) {
        return row.size() >= MAX_NUMERIC_ROW_SIZE;
    }

    private static InlineKeyboardButton createButton(String text, String data) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(data);
        return button;
    }
}
