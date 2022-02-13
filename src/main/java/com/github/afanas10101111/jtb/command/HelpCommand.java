package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.command.CommandName.HELP;
import static com.github.afanas10101111.jtb.command.CommandName.START;
import static com.github.afanas10101111.jtb.command.CommandName.STAT;
import static com.github.afanas10101111.jtb.command.CommandName.STOP;

@RequiredArgsConstructor
public class HelpCommand implements Command {
    public static final String MESSAGE = String.format("<b>Доступные команды:</b>\n" +
                    "%s - начать работу со мной\n" +
                    "%s - остановить работу со мной\n" +
                    "%s - узнать количество активных пользователей\n" +
                    "%s - получить помощь",
            START.getName(), STOP.getName(), STAT.getName(), HELP.getName());

    private final SendBotMessageService service;

    @Override
    public void execute(Update update) {
        service.sendMessage(update.getMessage().getChatId().toString(), MESSAGE);
    }
}
