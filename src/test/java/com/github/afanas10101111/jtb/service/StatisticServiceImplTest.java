package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.dto.GroupStatTo;
import com.github.afanas10101111.jtb.dto.StatisticTo;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StatisticServiceImplTest {
    private final GroupSubService groupSubService = Mockito.mock(GroupSubService.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final StatisticService statisticService = new StatisticServiceImpl(groupSubService, userService);

    @Test
    void calculateBotStatistic() {
        User user = new User();
        GroupSub groupSub = new GroupSub();
        user.setGroupSubs(Set.of(groupSub));
        groupSub.setId(8);
        groupSub.setTitle("Title");
        groupSub.setUsers(Set.of(user));

        Mockito.when(groupSubService.findAll()).thenReturn(List.of(groupSub));
        Mockito.when(userService.findAllActiveUsers()).thenReturn(List.of(user));
        Mockito.when(userService.findAllInactiveUsers()).thenReturn(List.of(new User()));

        StatisticTo statisticTo = statisticService.calculateBotStatistic();
        assertNotNull(statisticTo);
        assertEquals(1, statisticTo.getActiveUserCount());
        assertEquals(1, statisticTo.getInactiveUserCount());
        assertEquals(1.0, statisticTo.getAverageGroupCountByUser());
        assertEquals(
                List.of(new GroupStatTo(8, "Title", 1)),
                statisticTo.getGroupStatTos()
        );
    }
}
