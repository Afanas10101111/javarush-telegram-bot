package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.exception.UserNotFoundException;
import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static org.apache.commons.lang3.StringUtils.LF;

@RequiredArgsConstructor
public class SubscriptionsCommand implements Command {
    public static final String MESSAGE_FORMAT = "%s подписка на группы:\n\nИмя группы - ID группы\n\n%s";
    public static final String GROUP_TITLE_ID_FORMAT = "%s - %s";
    public static final String USER_IS_ACTIVE = "Активна";
    public static final String USER_IS_INACTIVE = "Неактивна";

    private final SendBotMessageService messageService;
    private final UserService userService;

    @Override
    public void execute(Update update) {
        String chatId = extractChatId(update);
        User user = userService.findByChatId(chatId).orElseThrow(UserNotFoundException::new);
        String activeSubs = user.getGroupSubs().stream()
                .map(sub -> String.format(GROUP_TITLE_ID_FORMAT, sub.getTitle(), sub.getId()))
                .collect(Collectors.joining(LF));
        messageService.sendMessage(
                chatId,
                String.format(MESSAGE_FORMAT, user.isActive() ? USER_IS_ACTIVE : USER_IS_INACTIVE, activeSubs)
        );
    }
}
