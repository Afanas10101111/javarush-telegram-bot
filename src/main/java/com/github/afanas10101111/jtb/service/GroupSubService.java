package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;

import java.util.List;
import java.util.Optional;

public interface GroupSubService {
    GroupSub save(User user, GroupDiscussionInfo groupDiscussionInfo);

    GroupSub save(GroupSub groupSub);

    Optional<GroupSub> findById(int id);

    List<GroupSub> findAll();
}
