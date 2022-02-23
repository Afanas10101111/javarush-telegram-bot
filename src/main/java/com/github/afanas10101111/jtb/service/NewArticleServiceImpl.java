package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.client.PostClient;
import com.github.afanas10101111.jtb.client.dto.PostInfo;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NewArticleServiceImpl implements NewArticleService {
    public static final String MESSAGE_FORMAT =
            "✨Вышла новая статья <b>%s</b> в группе <b>%s</b>.✨\n\n" +
                    "<b>Описание:</b> %s\n\n" +
                    "<b>Ссылка:</b> %s\n";
    public static final String WEB_POST_FORMAT = "https://javarush.ru/groups/posts/%s";

    private final SendBotMessageService sendMessageService;
    private final GroupSubService groupSubService;
    private final PostClient client;

    @Override
    @Transactional
    public void findAndNotify() {
        groupSubService.findAll().forEach(
                g -> {
                    List<PostInfo> newPosts = client.findNewPosts(g.getId(), g.getLastArticleId());
                    setNewLastArticleId(g, newPosts);
                    notifySubscribers(g, newPosts);
                }
        );
    }

    private void setNewLastArticleId(GroupSub groupSub, List<PostInfo> newPosts) {
        newPosts.stream()
                .mapToInt(PostInfo::getId)
                .max()
                .ifPresent(groupSub::setLastArticleId);
    }

    private void notifySubscribers(GroupSub groupSub, List<PostInfo> newPosts) {
        Collections.reverse(newPosts);
        List<String> messages = newPosts.stream()
                .map(post -> String.format(
                        MESSAGE_FORMAT,
                        post.getTitle(), groupSub.getTitle(), post.getDescription(), getPostUrl(post.getKey())
                ))
                .collect(Collectors.toList());
        groupSub.getUsers().stream()
                .filter(User::isActive)
                .forEach(u -> messages
                        .forEach(m -> sendMessageService.sendMessage(u.getChatId(), m)));
    }

    private String getPostUrl(String key) {
        return String.format(WEB_POST_FORMAT, key);
    }
}
