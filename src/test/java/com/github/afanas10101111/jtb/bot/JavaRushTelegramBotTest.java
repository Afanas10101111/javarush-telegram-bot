package com.github.afanas10101111.jtb.bot;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.command.Command;
import com.github.afanas10101111.jtb.exception.UserNotFoundException;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.StatisticService;
import com.github.afanas10101111.jtb.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Field;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class JavaRushTelegramBotTest {
    private JavaRushTelegramBot bot;

    @Mock
    private Command userNotFoundExceptionCommand;

    @Mock
    private Command otherExceptionCommand;

    @Mock
    private Update update;

    @BeforeEach
    void initBot() throws NoSuchFieldException, IllegalAccessException {
        UserService userService = Mockito.mock(UserService.class);
        GroupSubService groupSubService = Mockito.mock(GroupSubService.class);
        StatisticService statisticService = Mockito.mock(StatisticService.class);
        GroupClient client = Mockito.mock(GroupClient.class);

        userNotFoundExceptionCommand = Mockito.mock(Command.class);
        otherExceptionCommand = Mockito.mock(Command.class);
        update = Mockito.mock(Update.class);
        bot = new JavaRushTelegramBot(
                userService,
                groupSubService,
                statisticService,
                client,
                Set.of("Admin"),
                Set.of("Greeting")
        );
        setBotField(bot, "userNotFoundExceptionCommand", userNotFoundExceptionCommand);
        setBotField(bot, "otherExceptionCommand", otherExceptionCommand);
    }

    @Test
    void userNotFoundExceptionShouldExecuteCorrectCommand() {
        Mockito.when(update.hasMessage()).thenThrow(new UserNotFoundException());
        bot.onUpdateReceived(update);
        Mockito.verify(userNotFoundExceptionCommand).execute(update);
    }

    @Test
    void anyNotDefinedExceptionShouldExecuteCorrectCommand() {
        Mockito.when(update.hasMessage()).thenThrow(new NullPointerException());
        bot.onUpdateReceived(update);
        Mockito.verify(otherExceptionCommand).execute(update);
    }

    private void setBotField(JavaRushTelegramBot bot, String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = bot.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(bot, value);
    }
}
