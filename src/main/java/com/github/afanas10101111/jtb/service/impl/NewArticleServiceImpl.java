package com.github.afanas10101111.jtb.service.impl;

import com.github.afanas10101111.jtb.client.PostClient;
import com.github.afanas10101111.jtb.client.dto.PostInfo;
import com.github.afanas10101111.jtb.exception.BotBlockedByUserException;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.NewArticleService;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.afanas10101111.jtb.command.Emoji.COFFEE_SIGN;

@Slf4j
@RequiredArgsConstructor
@Service
public class NewArticleServiceImpl implements NewArticleService {
    public static final String MESSAGE_FORMAT = COFFEE_SIGN.getTextValue() +
            " В группе <b>%s</b> вышла новая статья:\n" +
            "<b>%s</b>\n\n" +
            "<b>Описание:</b> %s\n\n" +
            "<b>Ссылка:</b> %s\n";
    public static final String WEB_POST_FORMAT = "https://javarush.ru/groups/posts/%s";

    private final SendBotMessageService sendMessageService;
    private final GroupSubService groupSubService;
    private final PostClient postClient;

    @Override
    @Transactional
    public void findAndNotify() {
        groupSubService.findAll().forEach(
                g -> {
                    List<PostInfo> newPosts = postClient.findNewPosts(g.getId(), g.getLastArticleId());
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
                        groupSub.getTitle(), post.getTitle(), post.getDescription(), getPostUrl(post.getKey())
                ))
                .collect(Collectors.toList());
        groupSub.getUsers().stream()
                .filter(User::isActive)
                .forEach(u -> sendMessages(u, messages));
    }

    private String getPostUrl(String key) {
        return String.format(WEB_POST_FORMAT, key);
    }

    private void sendMessages(User user, List<String> messages) {
        try {
            messages.forEach(m -> sendMessageService.sendMessage(user.getChatId(), m));
        } catch (BotBlockedByUserException e) {
            log.warn(
                    "notifySubscribers -> User with chatId={} has blocked me and will be marked as inactive",
                    user.getChatId()
            );
            user.setActive(false);
        }
    }
}
