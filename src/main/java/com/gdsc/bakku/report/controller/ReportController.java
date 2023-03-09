package com.gdsc.bakku.report.controller;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.domain.entity.Bakku;
import com.gdsc.bakku.bakku.service.BakkuService;
import com.gdsc.bakku.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    private final BakkuService bakkuService;

    @PostMapping("/bakku/{id}/reports")
    public ResponseEntity<Void> save(@PathVariable(name = "id") Long id, @AuthenticationPrincipal User user) {
        Bakku bakku = bakkuService.findEntityById(id);

        reportService.save(bakku, user);

        return ResponseEntity.ok().build();
    }
}
