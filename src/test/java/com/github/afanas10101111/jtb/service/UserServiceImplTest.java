package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static com.github.afanas10101111.jtb.service.UseDataBase.CHAT_ID_00;
import static com.github.afanas10101111.jtb.service.UseDataBase.NEW_CHAT_ID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@UseDataBase
@Import(UserServiceImpl.class)
class UserServiceImplTest {

    @Autowired
    private UserService service;

    @Test
    void saveAndFindByChatId() {
        User user = new User();
        user.setChatId(NEW_CHAT_ID);
        user.setActive(true);
        service.save(user);
        assertEquals(6, service.findAllActiveUsers().size());
        assertDoesNotThrow(() -> service.findByChatId(NEW_CHAT_ID).orElseThrow());
    }

    @Test
    void retrieveAllActiveUsersAndUpdateUser() {
        assertEquals(5, service.findAllActiveUsers().size());
        assertEquals(3, service.findAllInactiveUsers().size());
        User user = service.findByChatId(CHAT_ID_00).orElseThrow();
        user.setActive(false);
        service.save(user);
        assertEquals(4, service.findAllActiveUsers().size());
        assertEquals(4, service.findAllInactiveUsers().size());
    }
}
