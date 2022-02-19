package com.github.afanas10101111.jtb.service;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles("test")
@DataJpaTest
@Sql(scripts = "/sql/prepareForTest.sql", config = @SqlConfig(encoding = "UTF-8"))
public @interface UseDataBase {
    String NEW_CHAT_ID = "0987654321";
    String CHAT_ID_00 = "12345678900";
    String CHAT_ID_01 = "12345678901";
}
