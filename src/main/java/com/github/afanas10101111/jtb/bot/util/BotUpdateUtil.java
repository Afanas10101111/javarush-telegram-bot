package com.github.afanas10101111.jtb.bot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.apache.commons.lang3.StringUtils.SPACE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotUpdateUtil {
    public static String extractChatId(Update update) {
        return update.getMessage().getChatId().toString();
    }

    public static String extractUsername(Update update) {
        return update.getMessage().getFrom().getUserName();
    }

    public static String extractMessage(Update update) {
        return update.getMessage().getText();
    }

    public static String extractMessageArgument(Update update, int position) {
        return extractMessage(update).trim().split(SPACE)[position];
    }

    public static String extractCommandIdentifier(Update update) {
        return extractMessageArgument(update, 0);
    }

    public static boolean isMessageExists(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }
}
