package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.github.afanas10101111.jtb.command.CommandName.NOTIFY;

class NotifyUsersCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return NOTIFY.getName();
    }

    @Override
    String getCommandMessage() {
        return NotifyUsersCommand.MESSAGE;
    }

    @Override
    Command getCommand() {
        return new NotifyUsersCommand(messageService, userService);
    }

    @Test
    void shouldSendMessageToAllActiveUsers() {
        User user = new User();
        user.setChatId(CHAT_ID.toString());
        Mockito.when(userService.findAllActiveUsers()).thenReturn(List.of(user));

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        String messageForUsers = "The Bot is going crazy";
        Mockito.when(message.getText()).thenReturn(getCommandName() + " " + messageForUsers);
        update.setMessage(message);

        getCommand().execute(update);
        Mockito.verify(messageService).sendMessage(CHAT_ID.toString(), messageForUsers);
    }
}
