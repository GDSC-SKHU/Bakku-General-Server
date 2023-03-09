package com.gdsc.bakku.report.domain.repo;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.domain.entity.Bakku;
import com.gdsc.bakku.report.domain.entitiy.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByBakkuAndUser(Bakku bakku, User user);

    List<Report> findAllByBakku(Bakku bakku);

    void deleteAllByBakku(Bakku bakku);
}
