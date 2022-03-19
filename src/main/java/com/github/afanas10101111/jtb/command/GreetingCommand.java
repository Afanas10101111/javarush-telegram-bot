package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;

@RequiredArgsConstructor
public class GreetingCommand implements Command {
    public static final String MESSAGE = "И тебе привет \uD83D\uDC4B\n" +
            "У меня все под контролем! Как только выйдет интересующая тебя статья, ты сразу узнаешь об этом \uD83D\uDE09";

    private final SendBotMessageService service;

    @Override
    public void execute(Update update) {
        service.sendMessage(extractChatId(update), MESSAGE);
    }
}
