package com.github.afanas10101111.jtb.command;

import com.github.afanas10101111.jtb.client.GroupClient;
import com.github.afanas10101111.jtb.command.annotation.AdminCommand;
import com.github.afanas10101111.jtb.service.GroupSubService;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import com.github.afanas10101111.jtb.service.StatisticService;
import com.github.afanas10101111.jtb.service.UserService;
import com.google.common.collect.ImmutableMap;

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
import static com.github.afanas10101111.jtb.command.CommandName.UNSUBSCRIBE;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIPTIONS;
import static java.util.Objects.nonNull;

public class CommandContainer {
    private final Map<String, Command> commands;
    private final Command unknown;
    private final Set<String> admins;
    private final Set<String> knownGreetings;

    public CommandContainer(
            SendBotMessageService messageService,
            UserService userService,
            GroupSubService groupSubService,
            StatisticService statisticService,
            GroupClient client,
            Set<String> admins,
            Set<String> knownGreetings
    ) {
        this.admins = admins;
        this.knownGreetings = knownGreetings;
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
