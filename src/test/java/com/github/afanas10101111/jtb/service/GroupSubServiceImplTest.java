package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.client.GroupClientImpl;
import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.repository.GroupSubRepository;
import com.github.afanas10101111.jtb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static com.github.afanas10101111.jtb.service.UseDataBase.CHAT_ID_00;
import static com.github.afanas10101111.jtb.service.UseDataBase.CHAT_ID_01;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UseDataBase
@Import({GroupSubServiceImpl.class, UserServiceImpl.class, GroupClientImpl.class})
class GroupSubServiceImplTest {

    @Autowired
    private GroupSubService service;

    @Autowired
    private GroupSubRepository groupSubRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveNewGroupSub() {
        int newGroupId = 2;
        String newGroupTitle = "new";

        performCheck(newGroupId, newGroupTitle, 1, CHAT_ID_00);
    }

    @Test
    void saveExistedGroupSubForExistedUser() {
        int groupId = 1;
        String groupTitle = "g1";

        performCheck(groupId, groupTitle, 1, CHAT_ID_00);
        performCheck(groupId, groupTitle, 2, CHAT_ID_01);
    }

    @Test
    void saveGroup() {
        int groupId = 1;
        String groupTitle = "updated";

        GroupSub groupSub = new GroupSub();
        groupSub.setId(groupId);
        groupSub.setTitle(groupTitle);
        GroupSub saved = service.save(groupSub);

        checkGroupSub(saved, groupId, groupTitle, 0, null);
        checkGroupSub(service.findById(groupId).orElseThrow(), groupId, groupTitle, 0, null);
    }

    @Test
    void findAll() {
        assertThat(service.findAll()).asList().hasSize(1);
    }

    private void performCheck(int groupId, String groupTitle, int subscribersCount, String chatId) {
        GroupSub saved = service.save(chatId, createGroupDiscussionInfo(groupId, groupTitle));
        checkGroupSub(saved, groupId, groupTitle, subscribersCount, chatId);

        GroupSub fromDB = groupSubRepository.findById(groupId).orElseThrow();
        checkGroupSub(fromDB, groupId, groupTitle, subscribersCount, chatId);
    }

    private GroupDiscussionInfo createGroupDiscussionInfo(int groupId, String groupTitle) {
        GroupDiscussionInfo groupDiscussionInfo = new GroupDiscussionInfo();
        groupDiscussionInfo.setId(groupId);
        groupDiscussionInfo.setTitle(groupTitle);
        return groupDiscussionInfo;
    }

    private void checkGroupSub(GroupSub groupSub, int groupId, String groupTitle, int subscribersCount, String chatId) {
        assertEquals(groupId, groupSub.getId());
        assertEquals(groupTitle, groupSub.getTitle());
        assertEquals(subscribersCount, groupSub.getUsers().size());
        if (subscribersCount > 0) {
            assertTrue(groupSub.getUsers().contains(userRepository.getById(chatId)));
        }
    }
}
