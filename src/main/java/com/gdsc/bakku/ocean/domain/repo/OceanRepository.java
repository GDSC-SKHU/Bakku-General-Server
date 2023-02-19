package com.gdsc.bakku.ocean.domain.repo;

import com.gdsc.bakku.ocean.domain.entity.Ocean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OceanRepository extends JpaRepository<Ocean, Long> {
    Slice<Ocean> findAllBy(Pageable pageable);
}
