package com.github.afanas10101111.jtb.repository;

import com.github.afanas10101111.jtb.model.GroupSub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface GroupSubRepository extends JpaRepository<GroupSub, Integer> {
}

