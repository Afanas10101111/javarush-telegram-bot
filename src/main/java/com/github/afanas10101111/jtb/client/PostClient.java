package com.github.afanas10101111.jtb.client;

import com.github.afanas10101111.jtb.client.dto.PostInfo;

import java.util.List;

public interface PostClient {
    List<PostInfo> findNewPosts(Integer groupId, int lastPostId);
}
