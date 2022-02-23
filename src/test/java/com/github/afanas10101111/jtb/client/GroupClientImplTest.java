package com.github.afanas10101111.jtb.client;

import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.client.dto.GroupInfo;
import com.github.afanas10101111.jtb.client.dto.GroupRequestArgs;
import com.github.afanas10101111.jtb.client.dto.PostInfo;
import kong.unirest.HttpMethod;
import kong.unirest.MockClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.github.afanas10101111.jtb.client.GroupClientImpl.GROUP_BY_ID_PATH;
import static com.github.afanas10101111.jtb.client.GroupClientImpl.GROUP_COUNT_PATH;
import static com.github.afanas10101111.jtb.client.dto.GroupInfoType.TECH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GroupClientImplTest {
    private static final String URL = "https://javarush.ru/api/1.0/rest";

    private final GroupClient groupClient = new GroupClientImpl(URL);

    private static final String GROUPS = "/groups";
    private static final String POSTS = "/posts";
    private static final MockClient client = MockClient.register();

    @BeforeAll
    static void mockClientSetup() {
        client.expect(HttpMethod.GET, URL + GROUPS)
                .thenReturn(List.of(new GroupInfo()));
        client.expect(HttpMethod.GET, URL + GROUPS)
                .queryString("limit", "2")
                .thenReturn(List.of(new GroupInfo(), new GroupInfo()));

        client.expect(HttpMethod.GET, String.format(GROUP_COUNT_PATH, URL + GROUPS))
                .thenReturn(32);
        client.expect(HttpMethod.GET, String.format(GROUP_COUNT_PATH, URL + GROUPS))
                .queryString("type", "TECH")
                .thenReturn(7);

        GroupDiscussionInfo groupInfo = new GroupDiscussionInfo();
        groupInfo.setId(16);
        groupInfo.setType(TECH);
        groupInfo.setKey("android");
        client.expect(HttpMethod.GET, String.format(GROUP_BY_ID_PATH, URL + GROUPS, 16))
                .thenReturn(groupInfo);

        client.expect(HttpMethod.GET,  URL + POSTS)
                .queryString("groupKid", "16")
                .thenReturn(Collections.emptyList());

        PostInfo post = new PostInfo();
        post.setId(1);
        client.expect(HttpMethod.GET,  URL + POSTS)
                .queryString("groupKid", "15")
                .thenReturn(Collections.singletonList(post));
    }

    @Test
    void shouldProperlyGetGroupsWithEmptyArgs() {
        GroupRequestArgs args = GroupRequestArgs.builder().build();
        List<GroupInfo> groupList = groupClient.getGroupList(args);

        assertNotNull(groupList);
        assertFalse(groupList.isEmpty());
    }

    @Test
    void shouldProperlyGetWithOffSetAndLimit() {
        GroupRequestArgs args = GroupRequestArgs.builder()
                .offset(1)
                .limit(2)
                .build();
        List<GroupInfo> groupList = groupClient.getGroupList(args);

        assertNotNull(groupList);
        assertEquals(2, groupList.size());
    }

    @Test
    void shouldProperlyGetGroupsDiscWithEmptyArgs() {
        GroupRequestArgs args = GroupRequestArgs.builder().build();
        List<GroupDiscussionInfo> groupList = groupClient.getGroupDiscussionList(args);

        assertNotNull(groupList);
        assertFalse(groupList.isEmpty());
    }

    @Test
    void shouldProperlyGetGroupDiscWithOffSetAndLimit() {
        GroupRequestArgs args = GroupRequestArgs.builder()
                .offset(1)
                .limit(2)
                .build();
        List<GroupDiscussionInfo> groupList = groupClient.getGroupDiscussionList(args);

        assertNotNull(groupList);
        assertEquals(2, groupList.size());
    }

    @Test
    void shouldProperlyGetGroupCount() {
        GroupRequestArgs args = GroupRequestArgs.builder().build();
        Integer groupCount = groupClient.getGroupCount(args);

        assertEquals(32, groupCount);
    }

    @Test
    void shouldProperlyGetGroupTECHCount() {
        GroupRequestArgs args = GroupRequestArgs.builder()
                .type(TECH)
                .build();
        Integer groupCount = groupClient.getGroupCount(args);

        assertEquals(7, groupCount);
    }

    @Test
    void shouldProperlyGetGroupById() {
        GroupDiscussionInfo groupById = groupClient.getGroupById(16);

        assertNotNull(groupById);
        assertEquals(16, groupById.getId());
        assertEquals(TECH, groupById.getType());
        assertEquals("android", groupById.getKey());
    }

    @Test
    void shouldProperlyFindLastPostId() {
        assertEquals(0, groupClient.findLastPostId(16));
        assertEquals(1, groupClient.findLastPostId(15));
    }
}
