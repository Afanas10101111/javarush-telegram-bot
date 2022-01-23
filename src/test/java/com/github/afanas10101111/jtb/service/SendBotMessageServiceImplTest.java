package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.bot.JavaRushTelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

class SendBotMessageServiceImplTest {
    private static final String CHAT_ID = "test_chat_id";
    private static final String MESSAGE = "test_message";

    private final JavaRushTelegramBot bot = Mockito.mock(JavaRushTelegramBot.class);
    private final SendBotMessageService service = new SendBotMessageServiceImpl(bot);

    @Test
    void shouldProperlySendMessage() throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(MESSAGE);
        sendMessage.setChatId(CHAT_ID);
        sendMessage.enableHtml(true);

        service.sendMessage(CHAT_ID, MESSAGE);
        Mockito.verify(bot).execute(sendMessage);
    }
}
