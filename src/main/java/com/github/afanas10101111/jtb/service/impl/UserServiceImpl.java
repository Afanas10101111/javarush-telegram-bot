package com.github.afanas10101111.jtb.service.impl;

import com.github.afanas10101111.jtb.model.User;
import com.github.afanas10101111.jtb.repository.UserRepository;
import com.github.afanas10101111.jtb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public void save(User user) {
        repository.save(user);
    }

    @Override
    public List<User> findAllActiveUsers() {
        return repository.findAllByActiveTrue();
    }

    @Override
    public List<User> findAllInactiveUsers() {
        return repository.findAllByActiveFalse();
    }

    @Override
    public Optional<User> findByChatId(String chatId) {
        return repository.findById(chatId);
    }
}
