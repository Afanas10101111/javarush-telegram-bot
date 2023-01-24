package com.github.afanas10101111.jtb.client;

import com.github.afanas10101111.jtb.client.dto.PostInfo;
import com.github.afanas10101111.jtb.client.impl.PostClientImpl;
import kong.unirest.HttpMethod;
import kong.unirest.MockClient;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PostClientImplTest {
    private static final String URL = "https://javarush.ru/api/1.0/rest";
    private static final String POST_PATH = "/posts";
    private static final String GROUP_KID_PARAM = "groupKid";
    private static final int LAST_POST_ID = 1;
    private static final MockClient client = MockClient.register();

    private final PostClient postClient = new PostClientImpl(URL);

    @BeforeAll
    static void mockClientSetup() {
        PostInfo post = new PostInfo();
        post.setId(2);
        client.expect(HttpMethod.GET, URL + POST_PATH)
                .queryString(GROUP_KID_PARAM, "18")
                .thenReturn(Collections.singletonList(post));

        client.expect(HttpMethod.GET, URL + POST_PATH)
                .queryString(GROUP_KID_PARAM, "16")
                .thenReturn(List.of());

        client.expect(HttpMethod.GET, URL + POST_PATH)
                .queryString(GROUP_KID_PARAM, "14")
                .thenReturn(StringUtils.EMPTY);
    }

    @Test
    void findNewPosts() {
        List<PostInfo> newPosts = postClient.findNewPosts(18, LAST_POST_ID);
        assertThat(newPosts).asList().hasSize(1);

        List<PostInfo> outOfNewPosts = postClient.findNewPosts(16, LAST_POST_ID);
        assertThat(outOfNewPosts).asList().isEmpty();

        List<PostInfo> nullResponse = postClient.findNewPosts(14, LAST_POST_ID);
        assertThat(nullResponse).asList().isEmpty();
    }
}
