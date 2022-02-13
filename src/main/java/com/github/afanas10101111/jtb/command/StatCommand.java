package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class StatCommand implements Command {
    public static final String MESSAGE = "Активных пользователей: %d";

    private final SendBotMessageService messageService;
    private final UserService userService;

    @Override
    public void execute(Update update) {
        messageService.sendMessage(
                update.getMessage().getChatId().toString(),
                String.format(MESSAGE, userService.retrieveAllActiveUsers().size())
        );
    }
}
