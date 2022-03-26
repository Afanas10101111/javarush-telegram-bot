package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.Optional;

import static com.github.afanas10101111.jtb.command.CommandName.UNSUBSCRIBE;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class UnsubscribeCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return UNSUBSCRIBE.getName();
    }

    @Override
    String getCommandMessage() {
        return String.format(UnsubscribeCommand.INFORMATION_FORMAT, "");
    }

    @Override
    Command getCommand() {
        User user = new User();
        user.setGroupSubs(Collections.emptySet());
        Mockito.when(userService.findByChatId(anyString())).thenReturn(Optional.of(user));
        return new UnsubscribeCommand(messageService, userService, groupSubService);
    }

    @Test
    void shouldProperlyExecuteCommandWithAlphabeticalGroupId() {
        String groupId = "zero";
        performCheck(groupId, UnsubscribeCommand.GROUP_NOT_FOUND_FORMAT);
    }

    @Test
    void shouldProperlyExecuteCommandWithUnknownGroupId() {
        String groupId = "16";
        performCheck(groupId, UnsubscribeCommand.GROUP_NOT_FOUND_FORMAT);
    }

    @Test
    void correctExecute() {
        String groupId = "16";

        GroupSub groupSub = new GroupSub();
        groupSub.addUser(new User());
        groupSub.setTitle(groupId);
        Mockito.when(groupSubService.findById(anyInt())).thenReturn(Optional.of(groupSub));

        performCheck(groupId, UnsubscribeCommand.UNSUBSCRIBED_FORMAT);
    }

    private void performCheck(String groupId, String messageFormat) {
        Update update = getMockedUpdate();
        update.getMessage().setText(getCommandName() + " " + groupId);

        getCommand().execute(update);
        verifyMessageServiceCall(CHAT_ID.toString(), String.format(messageFormat, groupId));
    }
}
