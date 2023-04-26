package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.dto.GroupStatTo;
import com.github.afanas10101111.jtb.dto.StatisticTo;
import org.mockito.Mockito;

import java.util.List;

import static com.github.afanas10101111.jtb.command.CommandName.STAT;

class StatCommandTest extends AbstractCommandTest {
    private static final String TITLE = "Title";

    @Override
    String getCommandName() {
        return STAT.getName();
    }

    @Override
    String getCommandMessage() {
        return String.format(
                StatCommand.MESSAGE,
                1,
                2,
                1.0,
                String.format(StatCommand.GROUP_FORMAT, TITLE, 3, 4)
        );
    }

    @Override
    Command getCommand() {
        Mockito.when(statisticServiceMock.calculateBotStatistic()).thenReturn(new StatisticTo(
                1,
                2,
                List.of(new GroupStatTo(3, TITLE, 4L)),
                1.0
        ));
        return new StatCommand(messageServiceMock, statisticServiceMock);
    }
}
