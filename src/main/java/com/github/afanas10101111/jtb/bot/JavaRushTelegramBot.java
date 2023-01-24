package com.github.afanas10101111.jtb.bot;

import com.github.afanas10101111.jtb.command.CommandContainer;
import com.github.afanas10101111.jtb.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractCommandIdentifier;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractUsername;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.isMessageOrCallbackQueryExists;
import static com.github.afanas10101111.jtb.command.ExceptionCommandName.UNEXPECTED_EXCEPTION;
import static com.github.afanas10101111.jtb.command.ExceptionCommandName.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Component
public class JavaRushTelegramBot extends TelegramLongPollingBot {
    private final CommandContainer commandContainer;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (isMessageOrCallbackQueryExists(update)) {
                commandContainer.retrieveCommand(extractCommandIdentifier(update), extractUsername(update))
                        .execute(update);
            }
        } catch (UserNotFoundException e) {
            log.warn(
                    "onUpdateReceived -> UserNotFoundException has been occurred: {}.\n{}",
                    e.getMessage(),
                    Arrays.toString(e.getStackTrace())
            );
            commandContainer.retrieveExceptionCommand(USER_NOT_FOUND)
                    .execute(update);
        } catch (Exception e) {
            log.error(
                    "onUpdateReceived -> Exception has been occurred: {}.\n{}",
                    e.getMessage(),
                    Arrays.toString(e.getStackTrace()));
            commandContainer.retrieveExceptionCommand(UNEXPECTED_EXCEPTION)
                    .execute(update);
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
