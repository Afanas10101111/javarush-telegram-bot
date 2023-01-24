package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.command.annotation.AdminCommand;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.StatisticService;
import com.github.afanas10101111.jtb.service.UserService;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.afanas10101111.jtb.command.CommandName.ADMIN_HELP;
import static com.github.afanas10101111.jtb.command.CommandName.GREETING;
import static com.github.afanas10101111.jtb.command.CommandName.HELP;
import static com.github.afanas10101111.jtb.command.CommandName.KEYBOARD;
import static com.github.afanas10101111.jtb.command.CommandName.NOTIFY;
import static com.github.afanas10101111.jtb.command.CommandName.START;
import static com.github.afanas10101111.jtb.command.CommandName.STAT;
import static com.github.afanas10101111.jtb.command.CommandName.STOP;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIPTIONS;
import static com.github.afanas10101111.jtb.command.CommandName.UNSUBSCRIBE;
import static com.github.afanas10101111.jtb.command.ExceptionCommandName.UNEXPECTED_EXCEPTION;
import static com.github.afanas10101111.jtb.command.ExceptionCommandName.USER_NOT_FOUND;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;

@Slf4j
@Component
public class CommandContainer {
    private final Set<String> admins;
    private final Set<String> knownGreetings;
    private final Command unknown;
    private final Map<String, Command> commands;
    private final Map<ExceptionCommandName, Command> exceptionCommands;

    public CommandContainer(
            @Lazy SendBotMessageService messageService,
            UserService userService,
            GroupSubService groupSubService,
            StatisticService statisticService,
            GroupClient client,
            @Value("${bot.admin_names}") Set<String> admins,
            @Value("${bot.known_greetings}") Set<String> knownGreetings
    ) {
        Set<String> knownGreetingsUTF8 = new HashSet<>();
        knownGreetings.forEach(g -> knownGreetingsUTF8.add((new String(g.getBytes(ISO_8859_1), UTF_8)).toLowerCase()));
        log.info("constructor -> admins: {}; knownGreetings: {}", admins, knownGreetingsUTF8);

        this.knownGreetings = Collections.unmodifiableSet(knownGreetingsUTF8);
        this.admins = Collections.unmodifiableSet(admins);

        unknown = new UnknownCommand(messageService);
        commands = ImmutableMap.<String, Command>builder()
                .put(GREETING.getName().toLowerCase(), new GreetingCommand(messageService))
                .put(START.getName().toLowerCase(), new StartCommand(messageService, userService))
                .put(STOP.getName().toLowerCase(), new StopCommand(messageService, userService))
                .put(SUBSCRIBE.getName().toLowerCase(), new SubscribeCommand(messageService, userService, groupSubService, client))
                .put(SUBSCRIPTIONS.getName().toLowerCase(), new SubscriptionsCommand(messageService, userService))
                .put(UNSUBSCRIBE.getName().toLowerCase(), new UnsubscribeCommand(messageService, userService, groupSubService))
                .put(KEYBOARD.getName().toLowerCase(), new KeyboardCommand(messageService))
                .put(HELP.getName().toLowerCase(), new HelpCommand(messageService))
                .put(ADMIN_HELP.getName().toLowerCase(), new AdminHelpCommand(messageService))
                .put(STAT.getName().toLowerCase(), new StatCommand(messageService, statisticService))
                .put(NOTIFY.getName().toLowerCase(), new NotifyUsersCommand(messageService, userService))
                .build();
        exceptionCommands = ImmutableMap.<ExceptionCommandName, Command>builder()
                .put(USER_NOT_FOUND, new UserNotFoundExceptionCommand(messageService))
                .put(UNEXPECTED_EXCEPTION, new OtherExceptionCommand(messageService, admins))
                .build();
    }

    public Command retrieveCommand(String commandIdentifier, String username) {
        String lowerCaseCommandIdentifier = commandIdentifier.toLowerCase();
        if (isGreeting(lowerCaseCommandIdentifier)) {
            return commands.get(GREETING.getName().toLowerCase());
        }
        Command command = commands.getOrDefault(lowerCaseCommandIdentifier, unknown);
        if (isAdminCommand(command)) {
            return isAdminUser(username) ? command : unknown;
        }
        return command;
    }

    public Command retrieveExceptionCommand(ExceptionCommandName exceptionCommandName) {
        return exceptionCommands.get(exceptionCommandName);
    }

    private boolean isAdminCommand(Command command) {
        return command != unknown && nonNull(command.getClass().getAnnotation(AdminCommand.class));
    }

    private boolean isAdminUser(String username) {
        return admins.contains(username);
    }

    private boolean isGreeting(String commandIdentifier) {
        for (String greeting : knownGreetings) {
            if (commandIdentifier.startsWith(greeting)) {
                return true;
            }
        }
        return false;
    }
}
