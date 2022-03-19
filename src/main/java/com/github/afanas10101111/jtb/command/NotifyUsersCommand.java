package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.command.annotation.AdminCommand;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractMessage;
import static com.github.afanas10101111.jtb.command.CommandName.NOTIFY;

@RequiredArgsConstructor
@AdminCommand
public class NotifyUsersCommand implements Command {
    public static final String MESSAGE = "Чтобы отправить сообщение пользователям, его нужно сначала написать \uD83D\uDE42\n" +
            "Например: " + NOTIFY.getName() + " Всем привет!\n" +
            "Все пользователи получат сообщение \"Всем привет!\"";

    private final SendBotMessageService messageService;
    private final UserService userService;
    private final int commandLength = NOTIFY.getName().length() + 1;

    @Override
    public void execute(Update update) {
        String message = extractMessage(update);
        if (isMessageForUsersEmpty(message)) {
            messageService.sendMessage(extractChatId(update), MESSAGE);
        } else {
            String messageForUsers = message.substring(commandLength).trim();
            userService.findAllActiveUsers().forEach(u -> messageService.sendMessage(u.getChatId(), messageForUsers));
        }
    }

    private boolean isMessageForUsersEmpty(String message) {
        return message.length() <= commandLength;
    }
}
