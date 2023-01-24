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
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.command.ExceptionCommandName.UNEXPECTED_EXCEPTION;
import static com.github.afanas10101111.jtb.command.ExceptionCommandName.USER_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class JavaRushTelegramBotTest {

    @Mock
    private CommandContainer commandContainer;

    @Mock
    private Command userNotFoundExceptionCommand;

    @Mock
    private Command otherExceptionCommand;

    @Mock
    private Update update;

    @InjectMocks
    private JavaRushTelegramBot bot;

    @Test
    void userNotFoundExceptionShouldExecuteCorrectCommand() {
        Mockito.when(update.hasMessage()).thenThrow(new UserNotFoundException());
        Mockito.when(commandContainer.retrieveExceptionCommand(USER_NOT_FOUND)).thenReturn(userNotFoundExceptionCommand);
        bot.onUpdateReceived(update);
        Mockito.verify(userNotFoundExceptionCommand).execute(update);
    }

    @Test
    void anyNotDefinedExceptionShouldExecuteCorrectCommand() {
        Mockito.when(update.hasMessage()).thenThrow(new NullPointerException());
        Mockito.when(commandContainer.retrieveExceptionCommand(UNEXPECTED_EXCEPTION)).thenReturn(otherExceptionCommand);
        bot.onUpdateReceived(update);
        Mockito.verify(otherExceptionCommand).execute(update);
    }
}
