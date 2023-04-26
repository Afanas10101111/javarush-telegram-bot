package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.dto.GroupStatTo;
import com.github.afanas10101111.jtb.dto.StatisticTo;
import com.github.afanas10101111.jtb.model.GroupSub;
import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.service.impl.StatisticServiceImpl;
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
    private GroupSubService groupSubServiceMock;

    @Mock
    private UserService userServiceMock;

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

        Mockito.when(groupSubServiceMock.findAll()).thenReturn(List.of(groupSub));
        Mockito.when(userServiceMock.findAllActiveUsers()).thenReturn(List.of(user));
        Mockito.when(userServiceMock.findAllInactiveUsers()).thenReturn(List.of(new User()));

        StatisticTo statisticTo = statisticService.calculateBotStatistic();
        assertNotNull(statisticTo);
        assertEquals(1, statisticTo.activeUserCount());
        assertEquals(1, statisticTo.inactiveUserCount());
        assertEquals(1.0, statisticTo.averageGroupCountByUser());
        assertEquals(
                List.of(new GroupStatTo(8, "Title", 0L)),
                statisticTo.groupStatTos()
        );
    }
}
