package com.github.afanas10101111.jtb.aspect;

import com.github.afanas10101111.jtb.exception.BotBlockedByUserException;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@ActiveProfiles("test")
@SpringBootTest(classes = ExceptionNotificationAspect.class)
@MockBean(SendBotMessageService.class)
class ExceptionNotificationAspectTest {
    private static final String NEW_CHAT_ID = "3";
    private static final String ONE_MORE_NEW_CHAT_ID = "4";

    @Autowired
    private SendBotMessageService messageService;

    @Autowired
    private ExceptionNotificationAspect aspect;

    private Map<String, LocalDateTime> linkToExceptionsMap;
    private Set<String> linkToNotifySet;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() throws NoSuchFieldException, IllegalAccessException {
        Field exceptionsMapField = aspect.getClass().getDeclaredField("exceptionsMap");
        exceptionsMapField.setAccessible(true);
        linkToExceptionsMap = (Map<String, LocalDateTime>) exceptionsMapField.get(aspect);
        linkToExceptionsMap.clear();

        Field chatIdToNotifyField = aspect.getClass().getDeclaredField("notifySet");
        chatIdToNotifyField.setAccessible(true);
        linkToNotifySet = (Set<String>) chatIdToNotifyField.get(aspect);
    }

    @Test
    void shouldNotifyAllAdminsOnce() {
        callNotifyAdmin(1, false);
        checkCallAndErrorMapSize(1, 1);
    }

    @Test
    void shouldNotifyAllAdminsOnceForSeveralSameExTheSameOur() {
        callNotifyAdmin(5, false);
        checkCallAndErrorMapSize(1, 1);
    }

    @Test
    void shouldNotifyAllAdminsAboutEachDifferentEx() {
        callNotifyAdmin(5, true);
        checkCallAndErrorMapSize(5, 5);
    }

    @Test
    void shouldNotifyAllAdminsAboutEachSameExInDifferentOurs() {
        callNotifyAdmin(1, false);
        linkToExceptionsMap.put((new RuntimeException()).toString(), LocalDateTime.now().minusHours(1));
        callNotifyAdmin(1, false);
        checkCallAndErrorMapSize(2, 1);
    }

    @Test
    void shouldRemoveLazyAdminFromNotifyList() {
        linkToNotifySet.add(NEW_CHAT_ID);
        linkToNotifySet.add(ONE_MORE_NEW_CHAT_ID);
        Mockito.doThrow(new BotBlockedByUserException(new RuntimeException()))
                .when(messageService).sendMessage(eq(NEW_CHAT_ID), anyString());
        callNotifyAdmin(1, false);
        assertThat(linkToNotifySet).doesNotContain(NEW_CHAT_ID);
        linkToNotifySet.remove(ONE_MORE_NEW_CHAT_ID);
    }

    private void callNotifyAdmin(int times, boolean needErrorMessage) {
        RuntimeException e;
        for (int i = 0; i < times; i++) {
            e = new RuntimeException(needErrorMessage ? String.valueOf(i) : null);
            aspect.notifyAdmin(e);
        }
    }

    private void checkCallAndErrorMapSize(int callTimes, int mapSize) {
        linkToNotifySet.forEach(
                id -> Mockito.verify(messageService, times(callTimes))
                        .sendMessage(eq(id), contains(RuntimeException.class.getSimpleName()))
        );
        assertThat(linkToExceptionsMap).hasSize(mapSize);
    }
}
