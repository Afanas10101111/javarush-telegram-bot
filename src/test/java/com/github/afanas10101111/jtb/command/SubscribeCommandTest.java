package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.client.dto.GroupInfo;
import com.github.afanas10101111.jtb.command.util.KeyboardUtil;
import com.github.afanas10101111.jtb.model.GroupSub;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static org.apache.commons.lang3.StringUtils.LF;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class SubscribeCommandTest extends AbstractCommandWithKeyboardTest {
    private static final String GROUP_TITLE = "title";
    private static final Integer GROUP_ID = 16;
    private static final String ALPHABETICAL_GROUP_ID = "zero";

    @Override
    String getCommandName() {
        return SUBSCRIBE.getName();
    }

    @Override
    String getCommandMessage() {
        return String.format(
                SubscribeCommand.INFORMATION_FORMAT,
                String.format(SubscribeCommand.GROUP_TITLE_ID_FORMAT, GROUP_TITLE, GROUP_ID)
        ) + LF;
    }

    @Override
    Command getCommand() {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setTitle(GROUP_TITLE);
        groupInfo.setId(GROUP_ID);
        Mockito.when(client.getGroupList(any())).thenReturn(List.of(groupInfo));
        return new SubscribeCommand(messageService, groupSubService, client);
    }

    @Override
    InlineKeyboardMarkup getKeyboard() {
        return KeyboardUtil.getNumericKeyboard(getCommandName(), List.of(GROUP_ID));
    }

    @Test
    void shouldProperlyExecuteCommandWithAlphabeticalGroupId() {
        performCheck(ALPHABETICAL_GROUP_ID, SubscribeCommand.GROUP_NOT_FOUND_FORMAT, null);
    }

    @Test
    void shouldProperlyExecuteCommandWithUnknownGroupId() {
        performCheck(GROUP_ID.toString(), SubscribeCommand.GROUP_NOT_FOUND_FORMAT, null);
    }

    @Test
    void correctExecute() {
        String groupId = GROUP_ID.toString();

        GroupSub savedGroupSub = new GroupSub();
        savedGroupSub.setTitle(groupId);
        Mockito.when(groupSubService.save(anyString(), any())).thenReturn(savedGroupSub);

        performCheck(groupId, SubscribeCommand.SUBSCRIBED_FORMAT, Integer.parseInt(groupId));
    }

    private void performCheck(String groupId, String messageFormat, Integer groupIdFromClient) {
        Update update = getMockedUpdate();
        update.getMessage().setText(getCommandName() + SPACE + groupId);

        GroupDiscussionInfo groupById = new GroupDiscussionInfo();
        groupById.setId(groupIdFromClient);
        Mockito.when(client.getGroupById(anyInt())).thenReturn(groupById);

        getCommand().execute(update);
        verifyMessageServiceCall(CHAT_ID.toString(), String.format(messageFormat, groupId));
    }
}
