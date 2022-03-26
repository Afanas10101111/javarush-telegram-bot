package com.github.afanas10101111.jtb.command.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyboardUtil {
    private static InlineKeyboardMarkup menuKeyboard;

    public static InlineKeyboardMarkup getMenuKeyboard() {
        if (menuKeyboard == null) {
            initializeMenuKeyboard();
        }
        return menuKeyboard;
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

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        menuKeyboard = inlineKeyboardMarkup;
    }

    private static InlineKeyboardButton createButton(String text, String data) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(data);
        return button;
    }
}
