package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.client.PostClient;
import com.github.afanas10101111.jtb.client.dto.PostInfo;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;

class NewArticleServiceImplTest {
    private final SendBotMessageService messageService = Mockito.mock(SendBotMessageService.class);
    private final GroupSubService groupSubService = Mockito.mock(GroupSubService.class);
    private final PostClient client = Mockito.mock(PostClient.class);
    private final NewArticleService newArticleService = new NewArticleServiceImpl(messageService, groupSubService, client);

    @Test
    void findAndNotify() {
        User user = new User();
        user.setChatId("1");
        user.setActive(true);
        GroupSub groupSub = new GroupSub();
        groupSub.setId(1);
        groupSub.setTitle("title");
        groupSub.setLastArticleId(1);
        groupSub.addUser(user);
        Mockito.when(groupSubService.findAll()).thenReturn(Collections.singletonList(groupSub));

        PostInfo post = new PostInfo();
        post.setId(2);
        post.setTitle("postTitle");
        post.setDescription("postDescription");
        post.setKey("postKey");
        Mockito.when(client.findNewPosts(anyInt(), anyInt())).thenReturn(Collections.singletonList(post));

        newArticleService.findAndNotify();
        Mockito.verify(messageService).sendMessage(user.getChatId(), String.format(
                NewArticleServiceImpl.MESSAGE_FORMAT,
                post.getTitle(), groupSub.getTitle(), post.getDescription(), String.format(NewArticleServiceImpl.WEB_POST_FORMAT, post.getKey())
                ));
    }
}
