package com.github.afanas10101111.jtb.bot;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.command.CommandContainer;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.SendBotMessageServiceImpl;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractCommandIdentifier;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.isMessageExists;

@Slf4j
@Setter
@Component
@ConfigurationProperties(prefix = "bot")
public class JavaRushTelegramBot extends TelegramLongPollingBot {
    private String username;
    private String token;

    private final CommandContainer commandContainer;

    public JavaRushTelegramBot(UserService userService, GroupSubService groupSubService, GroupClient client) {
        commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), userService, groupSubService, client);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(isMessageExists(update)) {
            commandContainer
                    .retrieveCommand(extractCommandIdentifier(update))
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
