package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.client.dto.GroupRequestArgs;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractMessage;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractMessageArgument;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.LF;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@RequiredArgsConstructor
public class SubscribeCommand implements Command {
    public static final String SUBSCRIBED_FORMAT = "Подписал на группу %s";
    public static final String GROUP_NOT_FOUND_FORMAT = "Нет группы с ID = %s";
    public static final String GROUP_TITLE_ID_FORMAT = "%s - %s";
    public static final String INFORMATION_FORMAT = "Чтобы подписаться на группу - передай еще и ID группы. " +
            "Например: " + SUBSCRIBE.getName() + " 16.\n" +
            "Вот список всех групп - выбирай какую хочешь :)\n\n" +
            "Имя группы - ID группы\n\n" +
            "%s";

    private final SendBotMessageService messageService;
    private final GroupSubService groupSubService;
    private final GroupClient client;

    @Override
    public void execute(Update update) {
        String message = extractMessage(update);
        String chatId = extractChatId(update);
        if (message.equalsIgnoreCase(SUBSCRIBE.getName())) {
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
            GroupSub savedGroupSub = groupSubService.save(chatId, groupById);
            messageService.sendMessage(chatId, String.format(SUBSCRIBED_FORMAT, savedGroupSub.getTitle()));
        } else {
            sendGroupNotFound(chatId, groupId);
        }
    }

    private void sendGroupNotFound(String chatId, String groupId) {
        messageService.sendMessage(chatId, String.format(GROUP_NOT_FOUND_FORMAT, groupId));
    }

    private void sendGroupIdList(String chatId) {
        String groupIds = client.getGroupList(GroupRequestArgs.builder().build()).stream()
                .map(group -> String.format(GROUP_TITLE_ID_FORMAT, group.getTitle(), group.getId()))
                .collect(Collectors.joining(LF));
        messageService.sendMessage(chatId, String.format(INFORMATION_FORMAT, groupIds));
    }
}
