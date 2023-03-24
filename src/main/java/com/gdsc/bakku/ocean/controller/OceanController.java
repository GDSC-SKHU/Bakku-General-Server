package com.gdsc.bakku.ocean.controller;

import com.gdsc.bakku.common.annotation.CustomPageableAsQueryParam;
import com.gdsc.bakku.common.exception.InvalidPositionException;
import com.gdsc.bakku.ocean.dto.OceanDTO;
import com.gdsc.bakku.ocean.service.OceanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OceanController {

    private final OceanService oceanService;

    @GetMapping("/oceans")
    @Operation(
            summary = "주변 바다들 조회",
            description = "파라미터로 입력받은 위도, 경도를 기반으로 해당 주변에있는 바다들을 조회합니다.",
            parameters = {
                    @Parameter(name = "lat", description = "위도", example = "37.4999969150419"),
                    @Parameter(name = "lon", description = "경도", example = "126.65099469811526")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", ref = "400")
            }

    )
    @CustomPageableAsQueryParam
    public ResponseEntity<Slice<OceanDTO>> findAll(@RequestParam(name = "lat") Double latitude,
                                                   @RequestParam(name = "lon") Double longitude,
                                                   @Parameter(hidden = true) @PageableDefault(size = 5) Pageable pageable) {
        if (!isPositionValid(latitude, longitude)) {
            throw new InvalidPositionException();
        }

        Slice<OceanDTO> oceans = oceanService.findAll(latitude, longitude, pageable);

        return ResponseEntity.ok(oceans);
    }

    @GetMapping("/oceans/{id}")
    @Operation(
            summary = "바다 한개 조회",
            description = "해당 ID의 바다를 하나 조회 합니다.",
            parameters = {
                    @Parameter(name = "id", description = "바다 ID", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }

    )
    public ResponseEntity<OceanDTO> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(oceanService.findById(id));
    }

    private boolean isPositionValid(Double latitude, Double longitude) {
        return (-90.0 <= latitude && latitude <= 90.0) &&
                (-180.0 <= longitude && longitude <= 180.0);
    }
}
