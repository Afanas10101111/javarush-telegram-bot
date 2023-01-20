package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.client.PostClient;
import com.github.afanas10101111.jtb.client.dto.PostInfo;
import com.github.afanas10101111.jtb.exception.BotBlockedByUserException;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

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

    private User user;
    private GroupSub groupSub;
    private PostInfo post;

    @BeforeEach
    void setup() {
        user = new User();
        user.setChatId("1");
        user.setActive(true);

        groupSub = new GroupSub();
        groupSub.setId(1);
        groupSub.setTitle("title");
        groupSub.setLastArticleId(1);
        groupSub.addUser(user);
        Mockito.when(groupSubService.findAll()).thenReturn(Collections.singletonList(groupSub));

        post = new PostInfo();
        post.setId(2);
        post.setTitle("postTitle");
        post.setDescription("postDescription");
        post.setKey("postKey");
        Mockito.when(client.findNewPosts(anyInt(), anyInt())).thenReturn(Collections.singletonList(post));
    }

    @Test
    void shouldFindAndNotify() {
        newArticleService.findAndNotify();
        Mockito.verify(messageService).sendMessage(user.getChatId(), String.format(
                NewArticleServiceImpl.MESSAGE_FORMAT,
                groupSub.getTitle(),
                post.getTitle(),
                post.getDescription(),
                String.format(NewArticleServiceImpl.WEB_POST_FORMAT, post.getKey())
        ));
        assertThat(user.isActive()).isTrue();
    }

    @Test
    void shouldMarkAsInactiveUserWhoBlockedTheBot() {
        Mockito.doThrow(new BotBlockedByUserException(new TelegramApiException()))
                .when(messageService)
                .sendMessage(anyString(), anyString());
        newArticleService.findAndNotify();
        assertThat(user.isActive()).isFalse();
    }
}
