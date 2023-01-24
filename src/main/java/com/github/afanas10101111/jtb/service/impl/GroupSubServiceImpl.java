package com.github.afanas10101111.jtb.service.impl;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.repository.GroupSubRepository;
import com.github.afanas10101111.jtb.service.GroupSubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupSubServiceImpl implements GroupSubService {
    private final GroupSubRepository repository;
    private final GroupClient client;

    @Override
    public GroupSub save(User user, GroupDiscussionInfo groupDiscussionInfo) {
        GroupSub groupSub;
        Optional<GroupSub> groupSubFromDB = repository.findById(groupDiscussionInfo.getId());
        if (groupSubFromDB.isPresent()) {
            groupSub = groupSubFromDB.get();
            Optional<User> subscribed = groupSub.getUsers().stream()
                    .filter(u -> u.equals(user))
                    .findFirst();
            if (subscribed.isEmpty()) {
                groupSub.addUser(user);
            }
        } else {
            groupSub = new GroupSub();
            groupSub.addUser(user);
            groupSub.setId(groupDiscussionInfo.getId());
            groupSub.setTitle(groupDiscussionInfo.getTitle());
            groupSub.setLastArticleId(client.findLastPostId(groupDiscussionInfo.getId()));
        }
        return repository.save(groupSub);
    }

    @Override
    public GroupSub save(GroupSub groupSub) {
        return repository.save(groupSub);
    }

    @Override
    public Optional<GroupSub> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<GroupSub> findAll() {
        return repository.findAll();
    }
}
