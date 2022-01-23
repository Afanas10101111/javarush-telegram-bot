package com.github.afanas10101111.jtb.bot;

import com.github.afanas10101111.jtb.command.CommandContainer;
import com.github.afanas10101111.jtb.service.SendBotMessageServiceImpl;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Setter
@Component
@ConfigurationProperties(prefix = "bot")
public class JavaRushTelegramBot extends TelegramLongPollingBot {
    private String username;
    private String token;

    private final CommandContainer commandContainer;

    public JavaRushTelegramBot() {
        commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this));
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            commandContainer
                    .retrieveCommand(update.getMessage().getText().trim().split(" ")[0].toLowerCase())
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
