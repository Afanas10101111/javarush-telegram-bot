package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.github.afanas10101111.jtb.command.CommandName.NOTIFY;
import static org.apache.commons.lang3.StringUtils.SPACE;

class NotifyUsersCommandTest extends AbstractCommandTest {
    private static final String MESSAGE_FOR_USERS = "The Bot is going crazy";

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

        Update update = getMockedUpdate();
        update.getMessage().setText(getCommandName() + SPACE + MESSAGE_FOR_USERS);

        getCommand().execute(update);
        verifyMessageServiceCall(CHAT_ID.toString(), MESSAGE_FOR_USERS);
    }
}
