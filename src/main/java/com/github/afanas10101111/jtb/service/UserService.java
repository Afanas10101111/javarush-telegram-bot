package com.github.afanas10101111.jtb.service;

import com.github.afanas10101111.jtb.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(User user);

    List<User> findAllActiveUsers();

    List<User> findAllInactiveUsers();

    Optional<User> findByChatId(String chatId);
}
