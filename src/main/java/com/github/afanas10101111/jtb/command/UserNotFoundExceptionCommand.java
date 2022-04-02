package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.command.CommandName.START;

@RequiredArgsConstructor
public class UserNotFoundExceptionCommand implements Command {
    public static final String MESSAGE = "Кажется, мы еще незнакомы \uD83D\uDE0E\n" +
            "Введи " + START.getName() + " чтоб представиться";

    private final SendBotMessageService service;

    @Override
    public void execute(Update update) {
        service.sendMessage(extractChatId(update), MESSAGE);
    }
}
