package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
@Sql(scripts = "/sql/prepareForTest.sql", config = @SqlConfig(encoding = "UTF-8"))
@Import(UserServiceImpl.class)
class UserServiceImplTest {
    private static final String NEW_CHAT_ID = "0987654321";
    private static final String CHAT_ID_00 = "12345678900";

    @Autowired
    private UserService service;

    @Test
    void saveAndFindByChatId() {
        User user = new User();
        user.setChatId(NEW_CHAT_ID);
        user.setActive(true);
        service.save(user);
        assertEquals(6, service.retrieveAllActiveUsers().size());
        assertDoesNotThrow(() -> service.findByChatId(NEW_CHAT_ID).orElseThrow());
    }

    @Test
    void retrieveAllActiveUsersAndUpdateUser() {
        assertEquals(5, service.retrieveAllActiveUsers().size());
        User user = service.findByChatId(CHAT_ID_00).orElseThrow();
        user.setActive(false);
        service.save(user);
        assertEquals(4, service.retrieveAllActiveUsers().size());
    }
}
