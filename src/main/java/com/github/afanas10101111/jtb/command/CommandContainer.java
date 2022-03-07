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
import static com.github.afanas10101111.jtb.command.CommandName.HELP;
import static com.github.afanas10101111.jtb.command.CommandName.START;
import static com.github.afanas10101111.jtb.command.CommandName.STAT;
import static com.github.afanas10101111.jtb.command.CommandName.STOP;
import static com.github.afanas10101111.jtb.command.CommandName.SUBSCRIBE;
import static com.github.afanas10101111.jtb.command.CommandName.UNSUBSCRIBE;
import static com.github.afanas10101111.jtb.command.CommandName.VIEW_SUBSCRIPTIONS;
import static java.util.Objects.nonNull;

public class CommandContainer {
    private final Map<String, Command> commands;
    private final Command unknown;
    private final Set<String> admins;

    public CommandContainer(
            SendBotMessageService messageService,
            UserService userService,
            GroupSubService groupSubService,
            StatisticService statisticService,
            GroupClient client,
            Set<String> admins
    ) {
        this.admins = admins;
        unknown = new UnknownCommand(messageService);
        commands = ImmutableMap.<String, Command>builder()
                .put(START.getName().toLowerCase(), new StartCommand(messageService, userService))
                .put(STOP.getName().toLowerCase(), new StopCommand(messageService, userService))
                .put(HELP.getName().toLowerCase(), new HelpCommand(messageService))
                .put(STAT.getName().toLowerCase(), new StatCommand(messageService, statisticService))
                .put(SUBSCRIBE.getName().toLowerCase(), new SubscribeCommand(messageService, groupSubService, client))
                .put(VIEW_SUBSCRIPTIONS.getName().toLowerCase(), new ViewSubscriptionsCommand(messageService, userService))
                .put(UNSUBSCRIBE.getName().toLowerCase(), new UnsubscribeCommand(messageService, userService, groupSubService))
                .put(ADMIN_HELP.getName().toLowerCase(), new AdminHelpCommand(messageService))
                .build();
    }

    public Command retrieveCommand(String commandIdentifier, String username) {
        Command command = commands.getOrDefault(commandIdentifier.toLowerCase(), unknown);
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
}
