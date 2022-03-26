package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractMessage;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractMessageArgument;
import static com.github.afanas10101111.jtb.command.CommandName.UNSUBSCRIBE;
import static com.github.afanas10101111.jtb.command.util.KeyboardUtil.getNumericKeyboard;
import static org.apache.commons.lang3.StringUtils.LF;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@RequiredArgsConstructor
public class UnsubscribeCommand implements Command {
    public static final String UNSUBSCRIBED_FORMAT = "Отписал от группы %s";
    public static final String GROUP_NOT_FOUND_FORMAT = "Среди твоих подписок нет группы с ID = %s";
    public static final String GROUP_TITLE_ID_FORMAT = "%s - %s";
    public static final String INFORMATION_FORMAT = "Передай ID группы, от которой хочешь отписаться. " +
            "Например: " + UNSUBSCRIBE.getName() + " 16\n" +
            "Или нажми на клавишу с нужным номером\n" +
            "Вот список твоих подписок:\n\n" +
            "Имя группы - ID группы\n\n" +
            "%s";

    private final SendBotMessageService messageService;
    private final UserService userService;
    private final GroupSubService groupSubService;

    @Override
    public void execute(Update update) {
        String message = extractMessage(update);
        String chatId = extractChatId(update);
        User user = userService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        if (message.equalsIgnoreCase(UNSUBSCRIBE.getName())) {
            sendGroupIdList(chatId, user);
            return;
        }
        String groupId = extractMessageArgument(update, 1);
        if (isNumeric(groupId)) {
            groupSubService.findById(Integer.parseInt(groupId)).ifPresentOrElse(
                    g -> {
                        g.getUsers().remove(user);
                        groupSubService.save(g);
                        messageService.sendMessage(chatId, String.format(UNSUBSCRIBED_FORMAT, g.getTitle()));
                    },
                    () -> sendGroupNotFound(chatId, groupId)
            );
        } else {
            sendGroupNotFound(chatId, groupId);
        }
    }

    private void sendGroupIdList(String chatId, User user) {
        StringBuilder groupIds = new StringBuilder();
        List<Integer> ids = new ArrayList<>();
        user.getGroupSubs().forEach(group -> {
            groupIds.append(String.format(GROUP_TITLE_ID_FORMAT, group.getTitle(), group.getId())).append(LF);
            ids.add(group.getId());
        });
        messageService.sendMessage(
                chatId,
                String.format(INFORMATION_FORMAT, groupIds.toString()),
                getNumericKeyboard(UNSUBSCRIBE.getName(), ids)
        );
    }

    private void sendGroupNotFound(String chatId, String groupId) {
        messageService.sendMessage(chatId, String.format(GROUP_NOT_FOUND_FORMAT, groupId));
    }
}
