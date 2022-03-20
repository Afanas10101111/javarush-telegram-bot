package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.command.annotation.AdminCommand;
import com.github.afanas10101111.jtb.dto.StatisticTo;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractUsername;

@Slf4j
@RequiredArgsConstructor
@AdminCommand
public class StatCommand implements Command {
    public static final String MESSAGE = "✨<b>Статистика</b>✨\n" +
            "- Количество активных пользователей: %s\n" +
            "- Количество неактивных пользователей: %s\n" +
            "- Среднее количество групп на одного пользователя: %s\n\n" +
            "<b>Информация по активным группам</b>:\n" +
            "%s";
    public static final String GROUP_FORMAT = "%s (id = %s) - %s подписчиков";

    private final SendBotMessageService messageService;
    private final StatisticService statisticService;

    @Override
    public void execute(Update update) {
        String chatId = extractChatId(update);
        log.info("execute -> {}(Id = {}) requested statistics", extractUsername(update), chatId);
        StatisticTo statisticTo = statisticService.calculateBotStatistic();
        String collectedGroups = statisticTo.getGroupStatTos().stream()
                .map(g -> String.format(GROUP_FORMAT, g.getTitle(), g.getId(), g.getActiveUserCount()))
                .collect(Collectors.joining("\n"));
        messageService.sendMessage(chatId, String.format(
                MESSAGE,
                statisticTo.getActiveUserCount(),
                statisticTo.getInactiveUserCount(),
                statisticTo.getAverageGroupCountByUser(),
                collectedGroups
        ));
    }
}
