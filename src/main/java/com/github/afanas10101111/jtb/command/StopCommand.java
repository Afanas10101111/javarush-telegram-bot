package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.exception.UserNotFoundException;
import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.transaction.Transactional;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;

@RequiredArgsConstructor
public class StopCommand implements Command {
    public static final String MESSAGE = "До свидания! Все твои подписки деактивированы";

    private final SendBotMessageService messageService;
    private final UserService userService;

    @Override
    @Transactional
    public void execute(Update update) {
        String chatId = extractChatId(update);
        User user = userService.findByChatId(chatId).orElseThrow(UserNotFoundException::new);
        user.setActive(false);
        userService.save(user);
        messageService.sendMessage(chatId, MESSAGE);
    }
}
