package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.command.util.KeyboardUtil;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.github.afanas10101111.jtb.command.CommandName.UNSUBSCRIBE;
import static org.apache.commons.lang3.StringUtils.LF;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class UnsubscribeCommandTest extends AbstractCommandWithKeyboardTest {
    private static final String GROUP_TITLE = "title";
    private static final Integer GROUP_ID = 16;
    private static final String ALPHABETICAL_GROUP_ID = "zero";

    @Override
    String getCommandName() {
        return UNSUBSCRIBE.getName();
    }

    @Override
    String getCommandMessage() {
        return String.format(
                UnsubscribeCommand.INFORMATION_FORMAT,
                String.format(SubscribeCommand.GROUP_TITLE_ID_FORMAT, GROUP_TITLE, GROUP_ID)
        ) + LF;
    }

    @Override
    Command getCommand() {
        GroupSub groupSub = new GroupSub();
        groupSub.setTitle(GROUP_TITLE);
        groupSub.setId(GROUP_ID);
        User user = new User();
        user.setGroupSubs(Set.of(groupSub));
        Mockito.when(userService.findByChatId(anyString())).thenReturn(Optional.of(user));

        return new UnsubscribeCommand(messageService, userService, groupSubService);
    }

    @Override
    InlineKeyboardMarkup getKeyboard() {
        return KeyboardUtil.getNumericKeyboard(getCommandName(), List.of(GROUP_ID));
    }

    @Test
    void shouldProperlyExecuteCommandWithAlphabeticalGroupId() {
        performCheck(ALPHABETICAL_GROUP_ID, UnsubscribeCommand.GROUP_NOT_FOUND_FORMAT);
    }

    @Test
    void shouldProperlyExecuteCommandWithUnknownGroupId() {
        performCheck(GROUP_ID.toString(), UnsubscribeCommand.GROUP_NOT_FOUND_FORMAT);
    }

    @Test
    void correctExecute() {
        String groupId = GROUP_ID.toString();

        GroupSub groupSub = new GroupSub();
        groupSub.addUser(new User());
        groupSub.setTitle(groupId);
        Mockito.when(groupSubService.findById(anyInt())).thenReturn(Optional.of(groupSub));

        performCheck(groupId, UnsubscribeCommand.UNSUBSCRIBED_FORMAT);
    }

    private void performCheck(String groupId, String messageFormat) {
        Update update = getMockedUpdate();
        update.getMessage().setText(getCommandName() + SPACE + groupId);

        getCommand().execute(update);
        verifyMessageServiceCall(CHAT_ID.toString(), String.format(messageFormat, groupId));
    }
}
