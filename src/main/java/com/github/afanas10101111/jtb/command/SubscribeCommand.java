package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.client.dto.GroupRequestArgs;
import com.github.afanas10101111.jtb.exception.UserNotFoundException;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractMessage;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractMessageArgument;
import static com.github.afanas10101111.jtb.client.dto.GroupOrder.GROUP_ID;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static com.github.afanas10101111.jtb.command.util.KeyboardUtil.getNumericKeyboard;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.LF;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@RequiredArgsConstructor
public class SubscribeCommand implements Command {
    public static final String SUBSCRIBED_FORMAT = "Подписал на группу %s";
    public static final String GROUP_NOT_FOUND_FORMAT = "Нет группы с ID = %s";
    public static final String GROUP_TITLE_ID_FORMAT = "%s - %s";
    public static final String SUBSCRIBE_INFORMATION_FORMAT = "Чтобы подписаться на группу, нажми на клавишу с нужным ID группы.\n" +
            "Вот список всех групп - выбирай \uD83D\uDE43\n\n" +
            "Имя группы - ID группы\n\n" +
            "%s";

    private final SendBotMessageService messageService;
    private final UserService userService;
    private final GroupSubService groupSubService;
    private final GroupClient client;

    @Override
    public void execute(Update update) {
        String chatId = extractChatId(update);
        User user = userService.findByChatId(chatId).orElseThrow(UserNotFoundException::new);
        if (extractMessage(update).equalsIgnoreCase(SUBSCRIBE.getName())) {
            sendGroupIdList(chatId);
            return;
        }
        String groupId = extractMessageArgument(update, 1);
        if (isNumeric(groupId)) {
            GroupDiscussionInfo groupById = client.getGroupById(Integer.parseInt(groupId));
            if (isNull(groupById.getId())) {
                sendGroupNotFound(chatId, groupId);
                return;
            }
            GroupSub savedGroupSub = groupSubService.save(user, groupById);
            messageService.sendMessage(chatId, String.format(SUBSCRIBED_FORMAT, savedGroupSub.getTitle()));
        } else {
            sendGroupNotFound(chatId, groupId);
        }
    }

    private void sendGroupIdList(String chatId) {
        StringBuilder groupIds = new StringBuilder();
        List<Integer> groupInfoIds = new ArrayList<>();
        client.getGroupList(GroupRequestArgs.builder().order(GROUP_ID).build()).forEach(groupInfo -> {
            groupIds.append(String.format(GROUP_TITLE_ID_FORMAT, groupInfo.getTitle(), groupInfo.getId())).append(LF);
            groupInfoIds.add(groupInfo.getId());
        });
        messageService.sendMessage(
                chatId,
                String.format(SUBSCRIBE_INFORMATION_FORMAT, groupIds.toString()),
                getNumericKeyboard(SUBSCRIBE.getName(), groupInfoIds)
        );
    }

    private void sendGroupNotFound(String chatId, String groupId) {
        messageService.sendMessage(chatId, String.format(GROUP_NOT_FOUND_FORMAT, groupId));
    }
}
