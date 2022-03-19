package com.github.afanas10101111.jtb.bot;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.command.CommandContainer;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.SendBotMessageServiceImpl;
import com.github.afanas10101111.jtb.service.StatisticService;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractCommandIdentifier;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractUsername;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.isMessageExists;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Setter
@Component
@ConfigurationProperties(prefix = "bot")
public class JavaRushTelegramBot extends TelegramLongPollingBot {
    private String username;
    private String token;

    private final CommandContainer commandContainer;

    public JavaRushTelegramBot(
            UserService userService,
            GroupSubService groupSubService,
            StatisticService statisticService,
            GroupClient client,
            @Value("${bot.admins}") Set<String> admins,
            @Value("${bot.known_greetings}") Set<String> knownGreetings
    ) {
        Set<String> knownGreetingsUTF8 = new HashSet<>();
        knownGreetings.forEach(g -> knownGreetingsUTF8.add((new String(g.getBytes(ISO_8859_1), UTF_8)).toLowerCase()));
        log.info("constructor -> admins: {}; knownGreetings: {}", admins, knownGreetingsUTF8);
        commandContainer = new CommandContainer(
                new SendBotMessageServiceImpl(this), userService, groupSubService, statisticService, client, admins, knownGreetingsUTF8
        );
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (isMessageExists(update)) {
            commandContainer.retrieveCommand(extractCommandIdentifier(update), extractUsername(update)).execute(update);
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
