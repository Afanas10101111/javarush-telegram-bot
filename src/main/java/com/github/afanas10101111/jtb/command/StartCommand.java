package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class StartCommand implements Command {
    public static final String MESSAGE = "Привет! Я готов помочь тебе быть в курсе выхода всех интересующих тебя статей на Java Rush";

    private final SendBotMessageService messageService;
    private final UserService userService;

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        userService.findByChatId(chatId).ifPresentOrElse(
                u -> {
                    u.setActive(true);
                    userService.save(u);
                },
                () -> {
                    User user = new User();
                    user.setChatId(chatId);
                    user.setActive(true);
                    userService.save(user);
                }
        );
        messageService.sendMessage(update.getMessage().getChatId().toString(), MESSAGE);
    }
}
