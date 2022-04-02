package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.model.User;
import org.mockito.Mockito;

import java.util.Optional;

import static com.github.afanas10101111.jtb.command.CommandName.STOP;

class StopCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return STOP.getName();
    }

    @Override
    String getCommandMessage() {
        return StopCommand.MESSAGE;
    }

    @Override
    Command getCommand() {
        String chatId = CHAT_ID.toString();
        User user = new User();
        user.setChatId(chatId);
        Mockito.when(userService.findByChatId(chatId)).thenReturn(Optional.of(user));
        return new StopCommand(messageService, userService);
    }
}
