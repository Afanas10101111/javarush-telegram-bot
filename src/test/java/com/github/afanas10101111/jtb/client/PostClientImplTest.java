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

import static com.github.afanas10101111.jtb.client.JavarushClientProperties.GROUP_KID_PARAM;
import static com.github.afanas10101111.jtb.client.JavarushClientProperties.JAVARUSH_URL;
import static com.github.afanas10101111.jtb.client.JavarushClientProperties.POSTS_PATH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PostClientImplTest {
    private static final MockClient clientMock = MockClient.register();
    private static final int LAST_POST_ID = 1;

    private final PostClient postClient = new PostClientImpl(JAVARUSH_URL);

    @BeforeAll
    static void mockClientSetup() {
        PostInfo post = new PostInfo();
        post.setId(2);
        clientMock.expect(HttpMethod.GET, JAVARUSH_URL + POSTS_PATH)
                .queryString(GROUP_KID_PARAM, "18")
                .thenReturn(Collections.singletonList(post));

        clientMock.expect(HttpMethod.GET, JAVARUSH_URL + POSTS_PATH)
                .queryString(GROUP_KID_PARAM, "16")
                .thenReturn(List.of());

        clientMock.expect(HttpMethod.GET, JAVARUSH_URL + POSTS_PATH)
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
