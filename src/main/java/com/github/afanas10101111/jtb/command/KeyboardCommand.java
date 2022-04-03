package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.command.Emoji.KEYS_SIGN;
import static com.github.afanas10101111.jtb.command.util.KeyboardUtil.getMenuKeyboard;

@RequiredArgsConstructor
public class KeyboardCommand implements Command {
    public static final String MESSAGE = KEYS_SIGN.getTextValue() +
            " <b>Клавиатура</b> " +
            KEYS_SIGN.getTextValue();

    private final SendBotMessageService messageService;

    @Override
    public void execute(Update update) {
        messageService.sendMessage(extractChatId(update), MESSAGE, getMenuKeyboard());
    }
}
