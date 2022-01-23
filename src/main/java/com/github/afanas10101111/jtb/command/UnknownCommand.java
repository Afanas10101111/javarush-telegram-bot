package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class UnknownCommand implements Command {
    public static final String MESSAGE = "Ничего не понял :(\nВведи /help чтобы ознакомиться со списком команд";

    private final SendBotMessageService service;

    @Override
    public void execute(Update update) {
        service.sendMessage(update.getMessage().getChatId().toString(), MESSAGE);
    }
}
