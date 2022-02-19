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

import static com.github.afanas10101111.jtb.command.CommandName.ADD_GROUP;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.LF;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@RequiredArgsConstructor
public class AddGroupCommand implements Command {
    public static final String SUBSCRIBED_FORMAT = "Подписал на группу %s";
    public static final String GROUP_NOT_FOUND_FORMAT = "Нет группы с ID = %s";
    public static final String GROUP_ID_TITLE_FORMAT = "%s - %s";
    public static final String INFORMATION_FORMAT = "Чтобы подписаться на группу - передай еще и ID группы. " +
            "Например: " + ADD_GROUP.getName() + " 16.\n" +
            "Вот список всех групп - выбирай какую хочешь :)\n\n" +
            "Имя группы - ID группы\n\n" +
            "%s";

    private final SendBotMessageService messageService;
    private final GroupSubService groupSubService;
    private final GroupClient client;

    @Override
    public void execute(Update update) {
        String message = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        if (message.equalsIgnoreCase(ADD_GROUP.getName())) {
            sendGroupIdList(chatId);
            return;
        }
        String groupId = message.split(SPACE)[1];
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
                .map(group -> String.format(GROUP_ID_TITLE_FORMAT, group.getTitle(), group.getId()))
                .collect(Collectors.joining(LF));
        messageService.sendMessage(chatId, String.format(INFORMATION_FORMAT, groupIds));
    }
}
