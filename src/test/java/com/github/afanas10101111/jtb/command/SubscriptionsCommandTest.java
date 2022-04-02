package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import org.mockito.Mockito;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIPTIONS;

class SubscriptionsCommandTest extends AbstractCommandTest {
    private static final int[] IDS = {1, 2};
    private static final String[] TITLES = {"first", "second"};

    @Override
    String getCommandName() {
        return SUBSCRIPTIONS.getName();
    }

    @Override
    String getCommandMessage() {
        return String.format(
                SubscriptionsCommand.MESSAGE_FORMAT,
                SubscriptionsCommand.USER_IS_INACTIVE,
                String.format(SubscriptionsCommand.GROUP_TITLE_ID_FORMAT + "\n", TITLES[0], IDS[0]) +
                        String.format(SubscriptionsCommand.GROUP_TITLE_ID_FORMAT, TITLES[1], IDS[1]));
    }

    @Override
    Command getCommand() {
        Set<GroupSub> groupSubs = new LinkedHashSet<>();
        groupSubs.add(createGroupSub(IDS[0], TITLES[0]));
        groupSubs.add(createGroupSub(IDS[1], TITLES[1]));
        User user = new User();
        user.setGroupSubs(groupSubs);
        Mockito.when(userService.findByChatId(CHAT_ID.toString())).thenReturn(Optional.of(user));
        return new SubscriptionsCommand(messageService, userService);
    }

    private GroupSub createGroupSub(Integer id, String title) {
        GroupSub groupSub = new GroupSub();
        groupSub.setId(id);
        groupSub.setTitle(title);
        return groupSub;
    }
}
