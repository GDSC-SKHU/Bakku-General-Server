package com.gdsc.bakku.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/ping")
    @Operation(
            summary = "인증 여부 확인",
            description = "header에 보낸 jwt 토큰이 서버에 인증 되는지 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "401", ref = "401")
            }
    )
    public ResponseEntity<Map<String, Object>> ping() {
        return ResponseEntity.ok(Map.of("isLogin", true));
    }
}
