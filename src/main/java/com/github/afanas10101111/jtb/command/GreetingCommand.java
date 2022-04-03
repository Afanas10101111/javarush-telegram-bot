package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.command.Emoji.GREETING_SIGN;
import static com.github.afanas10101111.jtb.command.Emoji.WINKING_SMILE;

@RequiredArgsConstructor
public class GreetingCommand implements Command {
    public static final String MESSAGE = "И тебе привет " + GREETING_SIGN.getTextValue() +
            "\nУ меня все под контролем! Как только выйдет интересующая тебя статья, ты сразу узнаешь об этом " +
            WINKING_SMILE.getTextValue();

    private final SendBotMessageService service;

    @Override
    public void execute(Update update) {
        service.sendMessage(extractChatId(update), MESSAGE);
    }
}
