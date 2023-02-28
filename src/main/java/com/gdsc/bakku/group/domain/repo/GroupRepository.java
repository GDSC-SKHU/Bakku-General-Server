package com.gdsc.bakku.group.domain.repo;

import com.gdsc.bakku.group.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group,Long> {
}
