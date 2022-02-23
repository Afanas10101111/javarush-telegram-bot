package com.github.afanas10101111.jtb.client;

import com.github.afanas10101111.jtb.client.dto.PostInfo;
import kong.unirest.HttpMethod;
import kong.unirest.MockClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PostClientImplTest {
    private static final String URL = "https://javarush.ru/api/1.0/rest";

    private final PostClient postClient = new PostClientImpl(URL);

    private static final String POSTS = "/posts";
    private static final MockClient client = MockClient.register();

    @BeforeAll
    static void mockClientSetup() {
        PostInfo post = new PostInfo();
        post.setId(2);
        client.expect(HttpMethod.GET, URL + POSTS)
                .queryString("groupKid", "18")
                .thenReturn(Collections.singletonList(post));

        client.expect(HttpMethod.GET, URL + POSTS)
                .queryString("groupKid", "16")
                .thenReturn(Collections.emptyList());
    }

    @Test
    void findNewPosts() {
        List<PostInfo> newPosts = postClient.findNewPosts(18, 1);
        assertThat(newPosts).asList().hasSize(1);

        List<PostInfo> outOfNewPosts = postClient.findNewPosts(16, Integer.MAX_VALUE);
        assertThat(outOfNewPosts).asList().isEmpty();
    }
}
