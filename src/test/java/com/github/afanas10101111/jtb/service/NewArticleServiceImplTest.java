package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.client.PostClient;
import com.github.afanas10101111.jtb.client.dto.PostInfo;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class NewArticleServiceImplTest {

    @Mock
    private SendBotMessageService messageService;

    @Mock
    private GroupSubService groupSubService;

    @Mock
    private PostClient client;

    @InjectMocks
    private NewArticleServiceImpl newArticleService;

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
                groupSub.getTitle(), post.getTitle(), post.getDescription(), String.format(NewArticleServiceImpl.WEB_POST_FORMAT, post.getKey())
                ));
    }
}
