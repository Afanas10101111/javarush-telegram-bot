package com.github.afanas10101111.jtb.aspect;

import com.github.afanas10101111.jtb.exception.BotBlockedByUserException;
import com.github.afanas10101111.jtb.service.SendBotMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = ExceptionNotificationAspect.class)
@MockBean(SendBotMessageService.class)
class ExceptionNotificationAspectTest {
    public static final String NEW_CHAT_ID = "8888";

    @Autowired
    private SendBotMessageService messageService;

    @Autowired
    private ExceptionNotificationAspect aspect;

    private Map<String, LocalDateTime> exceptionsMap;
    private Set<String> linkToChatIdToNotify;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() throws NoSuchFieldException, IllegalAccessException {
        Field exceptionsMapField = aspect.getClass().getDeclaredField("exceptionsMap");
        exceptionsMapField.setAccessible(true);
        exceptionsMap = (Map<String, LocalDateTime>) exceptionsMapField.get(aspect);
        exceptionsMap.clear();

        Field chatIdToNotifyField = aspect.getClass().getDeclaredField("chatIdToNotify");
        chatIdToNotifyField.setAccessible(true);
        linkToChatIdToNotify = (Set<String>) chatIdToNotifyField.get(aspect);
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
        exceptionsMap.put((new RuntimeException()).toString(), LocalDateTime.now().minusHours(1));
        callNotifyAdmin(1, false);
        checkCallAndErrorMapSize(2, 1);
    }

    @Test
    void shouldRemoveLazyAdminFromNotifyList() {
        linkToChatIdToNotify.add(NEW_CHAT_ID);
        Mockito.doThrow(new BotBlockedByUserException(new RuntimeException()))
                .when(messageService).sendMessage(eq(NEW_CHAT_ID), anyString());
        callNotifyAdmin(1, false);
        assertThat(linkToChatIdToNotify).doesNotContain(NEW_CHAT_ID);
    }

    private void callNotifyAdmin(int times, boolean needErrorMessage) {
        RuntimeException e;
        for (int i = 0; i < times; i++) {
            e = new RuntimeException(needErrorMessage ? String.valueOf(i) : null);
            aspect.notifyAdmin(e);
        }
    }

    private void checkCallAndErrorMapSize(int callTimes, int mapSize) {
        linkToChatIdToNotify.forEach(
                id -> Mockito.verify(messageService, times(callTimes))
                        .sendMessage(eq(id), contains(RuntimeException.class.getSimpleName()))
        );
        assertThat(exceptionsMap).hasSize(mapSize);
    }
}
