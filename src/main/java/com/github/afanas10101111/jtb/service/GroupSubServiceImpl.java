package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.repository.GroupSubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupSubServiceImpl implements GroupSubService {
    private final GroupSubRepository repository;
    private final UserService userService;

    @Override
    public GroupSub save(String chatId, GroupDiscussionInfo groupDiscussionInfo) {
        User user = userService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        GroupSub groupSub;
        Optional<GroupSub> groupSubFromDB = repository.findById(groupDiscussionInfo.getId());
        if (groupSubFromDB.isPresent()) {
            groupSub = groupSubFromDB.get();
            Optional<User> subscribed = groupSub.getUsers().stream()
                    .filter(u -> u.getChatId().equalsIgnoreCase(chatId))
                    .findFirst();
            if (subscribed.isEmpty()) {
                groupSub.addUser(user);
            }
        } else {
            groupSub = new GroupSub();
            groupSub.addUser(user);
            groupSub.setId(groupDiscussionInfo.getId());
            groupSub.setTitle(groupDiscussionInfo.getTitle());
        }
        return repository.save(groupSub);
    }
}
