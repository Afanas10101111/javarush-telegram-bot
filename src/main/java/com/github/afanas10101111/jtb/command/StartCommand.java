package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class StartCommand implements Command {
    private static final String MESSAGE = "Привет! Я готов помочь тебе быть в курсе выхода всех интересующих тебя статей на Java Rush";

    private final SendBotMessageService service;

    @Override
    public void execute(Update update) {
        service.sendMessage(update.getMessage().getChatId().toString(), MESSAGE);
    }
}
