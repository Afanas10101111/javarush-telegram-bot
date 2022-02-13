package com.github.afanas10101111.jtb.repository;

import com.github.afanas10101111.jtb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByActiveTrue();
}
