package com.github.afanas10101111.jtb.bot;

import com.github.afanas10101111.jtb.command.Command;
import com.github.afanas10101111.jtb.command.CommandContainer;
import com.github.afanas10101111.jtb.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.github.afanas10101111.jtb.command.ExceptionCommandName.UNEXPECTED_EXCEPTION;
import static com.github.afanas10101111.jtb.command.ExceptionCommandName.USER_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class JavaRushTelegramBotTest {
    private static final String TEXT = "text";
    private static final String NAME = "Spock";

    @Mock
    private CommandContainer commandContainerMock;

    @Mock
    private Command commandMock;

    @Mock
    private Update updateMock;

    @InjectMocks
    private JavaRushTelegramBot bot;

    @Test
    void shouldCorrectExecuteCommand() {
        User user = new User();
        user.setUserName(NAME);
        Message message = new Message();
        message.setText(TEXT);
        message.setFrom(user);

        Mockito.when(updateMock.getMessage()).thenReturn(message);
        Mockito.when(updateMock.hasMessage()).thenReturn(true);
        Mockito.when(commandContainerMock.retrieveCommand(TEXT, NAME)).thenReturn(commandMock);

        bot.onUpdateReceived(updateMock);
        Mockito.verify(commandMock).execute(updateMock);
    }

    @Test
    void userNotFoundExceptionShouldExecuteCorrectCommand() {
        Mockito.when(updateMock.hasMessage()).thenThrow(new UserNotFoundException());
        Mockito.when(commandContainerMock.retrieveExceptionCommand(USER_NOT_FOUND)).thenReturn(commandMock);

        bot.onUpdateReceived(updateMock);
        Mockito.verify(commandMock).execute(updateMock);
    }

    @Test
    void anyNotDefinedExceptionShouldExecuteCorrectCommand() {
        Mockito.when(updateMock.hasMessage()).thenThrow(new NullPointerException());
        Mockito.when(commandContainerMock.retrieveExceptionCommand(UNEXPECTED_EXCEPTION)).thenReturn(commandMock);

        bot.onUpdateReceived(updateMock);
        Mockito.verify(commandMock).execute(updateMock);
    }
}
