package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.StatisticService;
import com.github.afanas10101111.jtb.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

abstract class AbstractCommandTest {
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
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(CHAT_ID);
        Mockito.when(message.getText()).thenReturn(getCommandName());
        update.setMessage(message);

        getCommand().execute(update);
        Mockito.verify(messageService).sendMessage(CHAT_ID.toString(), getCommandMessage());
    }
}
