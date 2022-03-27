package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.bot.JavaRushTelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {
    private final JavaRushTelegramBot bot;

    @Override
    public void sendMessage(String chatId, String message) {
        sendMessage(chatId, message, null);
    }

    @Override
    public void sendMessage(String chatId, String message, InlineKeyboardMarkup keyboardMarkup) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.enableHtml(true);
        sm.setText(message);
        sm.setReplyMarkup(keyboardMarkup);
        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            log.error("sendMessage -> {}", e.getMessage());
        }
    }
}
