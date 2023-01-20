package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.bot.JavaRushTelegramBot;
import com.github.afanas10101111.jtb.exception.BotBlockedByUserException;
import com.github.afanas10101111.jtb.exception.BotExecuteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@RequiredArgsConstructor
@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {
    public static final String BOT_WAS_BLOCKED_BY_THE_USER = "Forbidden: bot was blocked by the user";

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
        } catch (TelegramApiRequestException e) {
            if (BOT_WAS_BLOCKED_BY_THE_USER.equals(e.getApiResponse())) {
                throw new BotBlockedByUserException(e);
            }
            throw new BotExecuteException(e);
        } catch (TelegramApiException e) {
            throw new BotExecuteException(e);
        }
    }
}
