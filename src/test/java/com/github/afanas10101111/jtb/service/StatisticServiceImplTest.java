package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.dto.GroupStatTo;
import com.github.afanas10101111.jtb.dto.StatisticTo;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class StatisticServiceImplTest {

    @Mock
    private GroupSubService groupSubService;

    @Mock
    private UserService userService;

    @InjectMocks
    private StatisticServiceImpl statisticService;

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
                List.of(new GroupStatTo(8, "Title", 0L)),
                statisticTo.getGroupStatTos()
        );
    }
}
