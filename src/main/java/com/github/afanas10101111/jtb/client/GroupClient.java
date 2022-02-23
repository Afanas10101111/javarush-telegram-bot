package com.github.afanas10101111.jtb.client;

import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.client.dto.GroupInfo;
import com.github.afanas10101111.jtb.client.dto.GroupRequestArgs;

import java.util.List;

public interface GroupClient {
    List<GroupInfo> getGroupList(GroupRequestArgs requestArgs);

    List<GroupDiscussionInfo> getGroupDiscussionList(GroupRequestArgs requestArgs);

    Integer getGroupCount(GroupRequestArgs requestArgs);

    GroupDiscussionInfo getGroupById(int id);

    Integer findLastPostId(int groupSubId);
}
