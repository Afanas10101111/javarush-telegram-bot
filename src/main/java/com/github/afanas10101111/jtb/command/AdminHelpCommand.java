package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.command.annotation.AdminCommand;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractUsername;
import static com.github.afanas10101111.jtb.command.CommandName.ADMIN_HELP;
import static com.github.afanas10101111.jtb.command.CommandName.NOTIFY;
import static com.github.afanas10101111.jtb.command.CommandName.STAT;
import static com.github.afanas10101111.jtb.command.Emoji.EXCLAMATION_SIGN;

@Slf4j
@RequiredArgsConstructor
@AdminCommand
public class AdminHelpCommand implements Command {
    public static final String MESSAGE = String.format("""
                    %s<b>Доступные для админа команды</b>%s
                                        
                    %s - узнать количество активных пользователей
                    %s - отправить сообщение всем активным пользователям
                    %s - получить список команд""",
            EXCLAMATION_SIGN.getTextValue(),
            EXCLAMATION_SIGN.getTextValue(),

            STAT.getName(),
            NOTIFY.getName(),
            ADMIN_HELP.getName());

    private final SendBotMessageService service;

    @Override
    public void execute(Update update) {
        String chatId = extractChatId(update);
        log.info("execute -> {}(Id = {}) requested help", extractUsername(update), chatId);
        service.sendMessage(chatId, MESSAGE);
    }
}
