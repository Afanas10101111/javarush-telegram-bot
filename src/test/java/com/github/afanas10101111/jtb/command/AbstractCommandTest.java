package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.StatisticService;
import com.github.afanas10101111.jtb.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

abstract class AbstractCommandTest {
    protected static final String TELEGRAM_USER_NAME = "DarthVader";
    protected static final Long CHAT_ID = 123456789L;

    protected SendBotMessageService messageService = Mockito.mock(SendBotMessageService.class);
    protected UserService userService = Mockito.mock(UserService.class);
    protected GroupSubService groupSubService = Mockito.mock(GroupSubService.class);
    protected StatisticService statisticService = Mockito.mock(StatisticService.class);
    protected GroupClient client = Mockito.mock(GroupClient.class);

    abstract String getCommandName();

    abstract String getCommandMessage();

    abstract Command getCommand();

    @Test
    void shouldProperlyExecuteCommand() {
        getCommand().execute(getMockedUpdate());
        verifyMessageServiceCall(CHAT_ID.toString(), getCommandMessage());
    }

    @Test
    void shouldProperlyExecuteCommandFromCallback() {
        getCommand().execute(getMockedUpdateWithCallback());
        verifyMessageServiceCall(CHAT_ID.toString(), getCommandMessage());
    }

    protected void verifyMessageServiceCall(String chatId, String message) {
        Mockito.verify(messageService).sendMessage(chatId, message);
    }

    protected void verifyMessageServiceCallWithKeyboard(String chatId, String message, InlineKeyboardMarkup keyboardMarkup) {
        Mockito.verify(messageService).sendMessage(chatId, message, keyboardMarkup);
    }

    protected Update getMockedUpdate() {
        Update update = new Update();
        update.setMessage(getMessage());
        return update;
    }

    protected Update getMockedUpdateWithCallback() {
        Update update = new Update();
        update.setCallbackQuery(getCallbackQuery());
        return update;
    }

    private CallbackQuery getCallbackQuery() {
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setData(getCommandName());
        callbackQuery.setMessage(getMessage());
        callbackQuery.setFrom(getTelegramUser());
        return callbackQuery;
    }

    private Message getMessage() {
        Message message = new Message();
        message.setChat(new Chat(CHAT_ID, ""));
        message.setText(getCommandName());
        message.setFrom(getTelegramUser());
        return message;
    }

    private org.telegram.telegrambots.meta.api.objects.User getTelegramUser() {
        org.telegram.telegrambots.meta.api.objects.User telegramUser = new org.telegram.telegrambots.meta.api.objects.User();
        telegramUser.setUserName(TELEGRAM_USER_NAME);
        return telegramUser;
    }
}
