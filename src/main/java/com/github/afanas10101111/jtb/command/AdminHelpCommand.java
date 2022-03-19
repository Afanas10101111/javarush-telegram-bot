package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.command.annotation.AdminCommand;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.command.CommandName.ADMIN_HELP;
import static com.github.afanas10101111.jtb.command.CommandName.NOTIFY;
import static com.github.afanas10101111.jtb.command.CommandName.STAT;

@RequiredArgsConstructor
@AdminCommand
public class AdminHelpCommand implements Command {
    public static final String MESSAGE = String.format("<b>✨Доступные для админа команды:✨</b>\n\n" +
                    "%s - узнать количество активных пользователей\n" +
                    "%s - отправить сообщение всем активным пользователям\n" +
                    "%s - получить помощь",
            STAT.getName(),
            NOTIFY.getName(),
            ADMIN_HELP.getName());

    private final SendBotMessageService service;

    @Override
    public void execute(Update update) {
        service.sendMessage(extractChatId(update), MESSAGE);
    }
}
