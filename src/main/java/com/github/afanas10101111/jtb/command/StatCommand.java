package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;

@RequiredArgsConstructor
public class StatCommand implements Command {
    public static final String MESSAGE = "Активных пользователей: %d";

    private final SendBotMessageService messageService;
    private final UserService userService;

    @Override
    public void execute(Update update) {
        messageService.sendMessage(
                extractChatId(update),
                String.format(MESSAGE, userService.retrieveAllActiveUsers().size())
        );
    }
}
