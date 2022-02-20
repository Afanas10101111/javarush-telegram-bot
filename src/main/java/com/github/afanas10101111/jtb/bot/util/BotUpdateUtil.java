package com.github.afanas10101111.jtb.bot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotUpdateUtil {
    public static String extractChatId(Update update) {
        return update.getMessage().getChatId().toString();
    }

    public static String extractMessage(Update update) {
        return update.getMessage().getText();
    }

    public static String extractCommandIdentifier(Update update) {
        return extractMessage(update).trim().split(" ")[0];
    }

    public static boolean isMessageExists(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }
}
