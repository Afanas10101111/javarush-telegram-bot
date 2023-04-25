package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;
import java.util.stream.Collectors;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.command.Emoji.TERRIFY_SMILE;
import static org.apache.commons.lang3.StringUtils.LF;

public class OtherExceptionCommand implements Command {
    public static final String MESSAGE = TERRIFY_SMILE.getTextValue() + """
             кажется, что-то пошло не так...
            Сообщи, пожалуйста, о данной неприятности одному из админов:
            """;
    public static final char TELEGRAM_USER_NAME_PREFIX = '@';

    private final SendBotMessageService service;
    private final String admins;

    public OtherExceptionCommand(SendBotMessageService service, Set<String> admins) {
        this.service = service;
        this.admins = admins.stream()
                .map(a -> TELEGRAM_USER_NAME_PREFIX + a)
                .collect(Collectors.joining(LF));
    }

    @Override
    public void execute(Update update) {
        service.sendMessage(extractChatId(update), MESSAGE + admins);
    }
}
