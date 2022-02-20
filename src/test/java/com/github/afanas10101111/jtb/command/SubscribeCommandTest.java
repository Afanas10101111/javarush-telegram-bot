package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.model.GroupSub;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static org.mockito.ArgumentMatchers.any;

class SubscribeCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return SUBSCRIBE.getName();
    }

    @Override
    String getCommandMessage() {
        return String.format(SubscribeCommand.INFORMATION_FORMAT, "");
    }

    @Override
    Command getCommand() {
        return new SubscribeCommand(messageService, groupSubService, client);
    }

    @Test
    void shouldProperlyExecuteCommandWithAlphabeticalGroupId() {
        String groupId = "zero";
        performCheck(groupId, SubscribeCommand.GROUP_NOT_FOUND_FORMAT, null);
    }

    @Test
    void shouldProperlyExecuteCommandWithUnknownGroupId() {
        String groupId = "16";
        performCheck(groupId, SubscribeCommand.GROUP_NOT_FOUND_FORMAT, null);
    }

    @Test
    void correctExecute() {
        String groupId = "16";

        GroupSub savedGroupSub = new GroupSub();
        savedGroupSub.setTitle(groupId);
        Mockito.when(groupSubService.save(any(), any())).thenReturn(savedGroupSub);

        performCheck(groupId, SubscribeCommand.SUBSCRIBED_FORMAT, Integer.parseInt(groupId));
    }

    private void performCheck(String groupId, String messageFormat, Integer groupIdFromClient) {
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(CHAT_ID);
        Mockito.when(message.getText()).thenReturn(getCommandName() + " " + groupId);

        GroupDiscussionInfo groupById = new GroupDiscussionInfo();
        groupById.setId(groupIdFromClient);
        Mockito.when(client.getGroupById(any())).thenReturn(groupById);

        update.setMessage(message);
        getCommand().execute(update);
        Mockito.verify(messageService).sendMessage(CHAT_ID.toString(), String.format(messageFormat, groupId));
    }
}
