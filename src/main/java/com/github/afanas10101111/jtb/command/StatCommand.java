package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.command.annotation.AdminCommand;
import com.github.afanas10101111.jtb.dto.GroupStatTo;
import com.github.afanas10101111.jtb.dto.StatisticTo;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.stream.Collectors;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractUsername;
import static com.github.afanas10101111.jtb.command.Emoji.EXCLAMATION_SIGN;

@Slf4j
@RequiredArgsConstructor
@AdminCommand
public class StatCommand implements Command {
    public static final String MESSAGE = EXCLAMATION_SIGN.getTextValue() +
            "<b>Статистика</b>" +
            EXCLAMATION_SIGN.getTextValue() + """
            
            - Количество активных пользователей: %s
            - Количество неактивных пользователей: %s
            - Среднее кол-во групп на 1 пользователя: %s
            
            <b>Количество подписчиков на группу</b>:
            %s""";
    public static final String GROUP_FORMAT = "%s (id = %s) - %s";

    private final SendBotMessageService messageService;
    private final StatisticService statisticService;

    @Override
    public void execute(Update update) {
        String chatId = extractChatId(update);
        log.info("execute -> {}(Id = {}) requested statistics", extractUsername(update), chatId);
        StatisticTo statisticTo = statisticService.calculateBotStatistic();
        String collectedGroups = statisticTo.groupStatTos().stream()
                .sorted(Comparator.comparingInt(GroupStatTo::id))
                .sorted(Comparator.comparingLong(GroupStatTo::activeUserCount).reversed())
                .map(g -> String.format(GROUP_FORMAT, g.title(), g.id(), g.activeUserCount()))
                .collect(Collectors.joining("\n"));
        messageService.sendMessage(chatId, String.format(
                MESSAGE,
                statisticTo.activeUserCount(),
                statisticTo.inactiveUserCount(),
                statisticTo.averageGroupCountByUser(),
                collectedGroups
        ));
    }
}
