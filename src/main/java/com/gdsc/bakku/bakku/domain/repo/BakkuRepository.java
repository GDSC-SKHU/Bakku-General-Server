package com.gdsc.bakku.bakku.domain.repo;

import com.gdsc.bakku.bakku.domain.entity.Bakku;
import com.gdsc.bakku.group.domain.entity.Group;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BakkuRepository extends JpaRepository<Bakku, Long> {
    Slice<Bakku> findAllByGroup(Group group, Pageable pageable);

    Slice<Bakku> findAllByOcean(Ocean ocean, Pageable pageable);
}
