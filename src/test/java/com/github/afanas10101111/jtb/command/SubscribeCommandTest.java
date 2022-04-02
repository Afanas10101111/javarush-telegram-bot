package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.client.dto.GroupInfo;
import com.github.afanas10101111.jtb.command.util.KeyboardUtil;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Optional;

import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static org.apache.commons.lang3.StringUtils.LF;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

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
                SubscribeCommand.SUBSCRIBE_INFORMATION_FORMAT,
                String.format(SubscribeCommand.GROUP_TITLE_ID_FORMAT, GROUP_TITLE, GROUP_ID)
        ) + LF;
    }

    @Override
    Command getCommand() {
        String chatId = CHAT_ID.toString();
        User user = new User();
        user.setChatId(chatId);
        Mockito.when(userService.findByChatId(chatId)).thenReturn(Optional.of(user));

        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setTitle(GROUP_TITLE);
        groupInfo.setId(GROUP_ID);
        Mockito.when(client.getGroupList(any())).thenReturn(List.of(groupInfo));

        return new SubscribeCommand(messageService, userService, groupSubService, client);
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
        Mockito.when(groupSubService.save(any(User.class), any())).thenReturn(savedGroupSub);

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
