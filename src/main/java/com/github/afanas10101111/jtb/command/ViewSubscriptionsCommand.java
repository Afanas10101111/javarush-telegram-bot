package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.stream.Collectors;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static org.apache.commons.lang3.StringUtils.LF;

@RequiredArgsConstructor
public class ViewSubscriptionsCommand implements Command {
    public static final String MESSAGE = "Активные подписки на группы:\n\nИмя группы - ID группы\n\n";
    public static final String GROUP_TITLE_ID_FORMAT = "%s - %s";

    private final SendBotMessageService messageService;
    private final UserService userService;

    @Override
    public void execute(Update update) {
        String chatId = extractChatId(update);
        User user = userService.findByChatId(chatId)
                .orElseThrow(NotFoundException::new);
        String activeSubs = user.getGroupSubs().stream()
                .map(sub -> String.format(GROUP_TITLE_ID_FORMAT, sub.getTitle(), sub.getId()))
                .collect(Collectors.joining(LF));
        messageService.sendMessage(chatId, MESSAGE + activeSubs);
    }
}
