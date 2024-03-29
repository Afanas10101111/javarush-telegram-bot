package com.github.afanas10101111.jtb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.afanas10101111.jtb.bot.JavaRushTelegramBot;
import com.github.afanas10101111.jtb.exception.BotBlockedByUserException;
import com.github.afanas10101111.jtb.service.impl.SendBotMessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class SendBotMessageServiceImplTest {
    private static final String CHAT_ID = "test_chat_id";
    private static final String MESSAGE = "test_message";

    @Mock
    private JavaRushTelegramBot botMock;

    @InjectMocks
    private SendBotMessageServiceImpl service;

    @Test
    void shouldProperlySendMessage() throws TelegramApiException {
        service.sendMessage(CHAT_ID, MESSAGE);
        Mockito.verify(botMock).execute(getSengMessage());
    }

    @Test
    void shouldThrowProperExceptionIfTheBotIsBlockedByUser() throws TelegramApiException, JsonProcessingException {
        ApiResponse<?> apiResponse = new ObjectMapper().readValue(
                "{\"description\":\"" + SendBotMessageServiceImpl.BOT_WAS_BLOCKED_BY_THE_USER + "\"}",
                ApiResponse.class
        );
        TelegramApiRequestException exception = new TelegramApiRequestException("", apiResponse);
        
        Mockito.when(botMock.execute(getSengMessage())).thenThrow(exception);

        assertThatExceptionOfType(BotBlockedByUserException.class)
                .isThrownBy(() -> service.sendMessage(CHAT_ID, MESSAGE));
    }

    private SendMessage getSengMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(MESSAGE);
        sendMessage.setChatId(CHAT_ID);
        sendMessage.enableHtml(true);
        return sendMessage;
    }
}
