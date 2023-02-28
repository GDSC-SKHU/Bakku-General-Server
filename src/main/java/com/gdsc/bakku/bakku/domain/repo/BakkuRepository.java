package com.gdsc.bakku.bakku.domain.repo;

import com.gdsc.bakku.bakku.domain.entity.Bakku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BakkuRepository extends JpaRepository<Bakku, Long> {
}
