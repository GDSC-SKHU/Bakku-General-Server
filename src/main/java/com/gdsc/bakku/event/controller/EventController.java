package com.gdsc.bakku.event.controller;

import com.gdsc.bakku.common.annotation.CustomPageableAsQueryParam;
import com.gdsc.bakku.event.dto.EventDTO;
import com.gdsc.bakku.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/events")
    @Operation(
            summary = "모든 이벤트 조회",
            description = "모든 이벤트를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "204", description = "데이터 없음")
            }
    )
    @CustomPageableAsQueryParam
    public ResponseEntity<Slice<EventDTO>> findAll(@Parameter(hidden = true) @PageableDefault(size = 5) Pageable pageable) {
        Slice<EventDTO> events = eventService.findAll(pageable);

        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/{id}")
    @Operation(
            summary = "특정 행사 조회",
            description = "해당 ID의 행사를 조회합니다.",
            parameters = {
                    @Parameter(name = "id", description = "행사 ID", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<EventDTO> findById(@PathVariable(name = "id") Long id) {
        EventDTO event = eventService.findById(id);

        return ResponseEntity.ok(event);
    }
}
