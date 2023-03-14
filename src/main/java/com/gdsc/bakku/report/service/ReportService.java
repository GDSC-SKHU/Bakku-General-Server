package com.gdsc.bakku.report.service;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.domain.entity.Bakku;
import com.gdsc.bakku.bakku.service.BakkuService;
import com.gdsc.bakku.common.exception.AlreadyReportException;
import com.gdsc.bakku.report.domain.entitiy.Report;
import com.gdsc.bakku.report.domain.repo.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    private final BakkuService bakkuService;

    @Transactional
    public void save(Long id, User user) {
        Bakku bakku = bakkuService.findEntityById(id);

        if (checkUser(bakku, user)) {
            throw new AlreadyReportException();
        }

        if (checkAndDelete(bakku)) {
            Report report = Report.builder()
                    .bakku(bakku)
                    .user(user)
                    .build();

            reportRepository.save(report);
        }
    }

    private boolean checkUser(Bakku bakku, User user) {
        return reportRepository.existsByBakkuAndUser(bakku, user);
    }

    private boolean checkAndDelete(Bakku bakku) {
        int numberOfReport =  reportRepository.findAllByBakku(bakku).size();

        if (numberOfReport >= 5) {
            reportRepository.deleteAllByBakku(bakku);

            bakkuService.deleteByEntity(bakku);

            return false;
        }

        return true;
    }
}

