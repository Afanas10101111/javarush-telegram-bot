package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.afanas10101111.jtb.bot.util.BotUpdateUtil.extractChatId;
import static com.github.afanas10101111.jtb.command.CommandName.HELP;
import static com.github.afanas10101111.jtb.command.CommandName.KEYBOARD;
import static com.github.afanas10101111.jtb.command.CommandName.START;
import static com.github.afanas10101111.jtb.command.CommandName.STOP;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIPTIONS;
import static com.github.afanas10101111.jtb.command.CommandName.UNSUBSCRIBE;
import static com.github.afanas10101111.jtb.command.Emoji.EXCLAMATION_SIGN;
import static com.github.afanas10101111.jtb.command.Emoji.GREETING_SIGN;
import static com.github.afanas10101111.jtb.command.Emoji.SMILE;

@RequiredArgsConstructor
public class HelpCommand implements Command {
    public static final String MESSAGE = String.format("%s<b>Доступные команды</b>%s" +

                    "\n\nНачать\\закончить работу с ботом:\n" +
                    "%s - начать работу со мной\n" +
                    "%s - остановить работу со мной\n\n" +

                    "Работа с подписками:\n" +
                    "%s - подписаться на группу статей\n" +
                    "%s - посмотреть активные подписки\n" +
                    "%s - отписаться от группы статей\n\n" +

                    "А еще можно:\n" +
                    "%s - вызвать клавиатуру\n" +
                    "%s - получить список команд\n" +
                    "и поздороваться %s попробуй %s",
            EXCLAMATION_SIGN.getTextValue(),
            EXCLAMATION_SIGN.getTextValue(),

            START.getName(),
            STOP.getName(),

            SUBSCRIBE.getName(),
            SUBSCRIPTIONS.getName(),
            UNSUBSCRIBE.getName(),

            KEYBOARD.getName(),
            HELP.getName(),

            SMILE.getTextValue(),
            GREETING_SIGN.getTextValue());

    private final SendBotMessageService service;

    @Override
    public void execute(Update update) {
        service.sendMessage(extractChatId(update), MESSAGE);
    }
}
