package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.dto.GroupStatTo;
import com.github.afanas10101111.jtb.dto.StatisticTo;
import com.github.afanas10101111.jtb.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {
    private final GroupSubService groupSubService;
    private final UserService userService;

    @Override
    public StatisticTo calculateBotStatistic() {
        List<GroupStatTo> groupStatTos = groupSubService.findAll().stream()
                .map(gs -> new GroupStatTo(gs.getId(), gs.getTitle(), getActiveUsersCount(gs.getUsers())))
                .collect(Collectors.toList());
        List<User> allInactiveUsers = userService.findAllInactiveUsers();
        List<User> allActiveUsers = userService.findAllActiveUsers();
        double groupsPerUser = getGroupsPerUser(allActiveUsers);
        return new StatisticTo(allActiveUsers.size(), allInactiveUsers.size(), groupStatTos, groupsPerUser);
    }

    private double getGroupsPerUser(List<User> allActiveUsers) {
        int allActiveUsersCount = allActiveUsers.isEmpty() ? 1 : allActiveUsers.size();
        return (double) allActiveUsers.stream().mapToInt(u -> u.getGroupSubs().size()).sum() / allActiveUsersCount;
    }

    private long getActiveUsersCount(Set<User> users) {
        return users.stream()
                .filter(User::isActive)
                .count();
    }
}
