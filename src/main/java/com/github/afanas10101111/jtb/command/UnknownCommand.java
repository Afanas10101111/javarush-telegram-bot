package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.command.Emoji.SAD_SMILE;

@RequiredArgsConstructor
public class UnknownCommand implements Command {
    public static final String MESSAGE = String.format("""
                    Ничего не понял %s
                    Введи /help чтобы ознакомиться со списком команд""",
            SAD_SMILE.getTextValue());

    private final SendBotMessageService service;

    @Override
    public void execute(Update update) {
        service.sendMessage(extractChatId(update), MESSAGE);
    }
}
