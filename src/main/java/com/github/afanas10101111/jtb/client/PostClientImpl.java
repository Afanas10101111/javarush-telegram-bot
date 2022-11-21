package com.github.afanas10101111.jtb.client;

import com.github.afanas10101111.jtb.client.dto.PostInfo;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PostClientImpl implements PostClient {
    private final String javarushApiPostPath;

    public PostClientImpl(@Value("${javarush.url}") String url) {
        this.javarushApiPostPath = url + "/posts";
    }

    @Override
    public List<PostInfo> findNewPosts(Integer groupId, int lastPostId) {
        List<PostInfo> newPosts = Unirest.get(javarushApiPostPath)
                .queryString("order", "NEW")
                .queryString("groupKid", groupId)
                .queryString("limit", 20)
                .asObject(new GenericType<List<PostInfo>>() {
                })
                .getBody();
        if (newPosts == null) {
            log.warn("GET {} returned null while a list is expected", javarushApiPostPath);
            return Collections.emptyList();
        } else {
            return newPosts.stream()
                    .filter(p -> p.getId() > lastPostId)
                    .collect(Collectors.toList());
        }
    }
}
