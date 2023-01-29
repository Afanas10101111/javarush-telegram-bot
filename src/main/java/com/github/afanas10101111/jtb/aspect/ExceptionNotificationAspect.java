package com.github.afanas10101111.jtb.aspect;

import com.github.afanas10101111.jtb.exception.BotBlockedByUserException;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.github.afanas10101111.jtb.command.Emoji.SHIT;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class ExceptionNotificationAspect {
    private final SendBotMessageService sendBotMessageService;

    @Value("${bot.admin_chat_ids}")
    private Set<String> chatIdsToNotify;

    private Map<String, LocalDateTime> exceptionsMap;
    private Set<String> notifySet;

    @PostConstruct
    private void setup() {
        exceptionsMap = new ConcurrentHashMap<>();
        notifySet = new ConcurrentSkipListSet<>(chatIdsToNotify);
    }

    @Pointcut("execution(* com.github.afanas10101111.jtb.service.impl.*.*(..))")
    private void services() {
    }

    @AfterThrowing(pointcut = "services()", throwing = "e")
    public void notifyAdmin(Exception e) {
        LocalDateTime eRiseTime = exceptionsMap.getOrDefault(e.toString(), LocalDateTime.MIN);
        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.HOURS.between(eRiseTime, now) > 0) {
            exceptionsMap.put(e.toString(), now);
            notifySet.forEach(id -> sendErrorInfo(id, e));
            log.warn("notifyAdmins -> Message about an exception was sent to {}", notifySet);
        }
    }

    private void sendErrorInfo(String id, Exception e) {
        try {
            sendBotMessageService.sendMessage(
                    id, String.format("%s%s%s%n%s", SHIT.getTextValue(), SHIT.getTextValue(), SHIT.getTextValue(), e)
            );
        } catch (BotBlockedByUserException ex) {
            log.warn("sendErrorInfo -> {} has blocked me and will be removed from notification list", id);
            notifySet.remove(id);
        }
    }
}
