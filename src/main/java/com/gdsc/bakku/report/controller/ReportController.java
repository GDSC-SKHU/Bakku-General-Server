package com.gdsc.bakku.report.controller;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @PostMapping(value = "/bakkus/{id}/reports")
    @Operation(
            summary = "바꾸 신고하기",
            description = "해당 ID의 바꾸를 신고합니다.",
            parameters = {
                    @Parameter(name = "id", description = "바꾸 ID", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "신고 성공"),
                    @ApiResponse(responseCode = "400", ref = "400"),
                    @ApiResponse(responseCode = "403", ref = "403")
            }
    )
    public ResponseEntity<Void> save(@PathVariable(name = "id") Long id, @AuthenticationPrincipal User user) {
        reportService.save(id, user);

        return ResponseEntity.ok().build();
    }
}
