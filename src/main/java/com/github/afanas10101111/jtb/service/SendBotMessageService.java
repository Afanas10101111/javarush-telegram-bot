package com.github.afanas10101111.jtb.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface SendBotMessageService {
    void sendMessage(String chatId, String message);

    void sendMessage(String chatId, String message, InlineKeyboardMarkup keyboardMarkup);
}
