package com.gdsc.bakku.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "행사 응답")
public record EventDTO(
        @Schema(description = "PK", example = "1")
        Long id,
        @Schema(description = "이름", example = "행사 이름")
        String name,
        @Schema(description = "행사 시작 시간", example = "2023-02-27T20:44:28")
        LocalDateTime startDate,
        @Schema(description = "행사 종료 시간", example = "2023-02-28T20:44:28")
        LocalDateTime endDate,
        @Schema(description = "내용", example = "행사 내용")
        String comment,
        @Schema(description = "위도", example = "37.58122552547526")
        Double latitude,
        @Schema(description = "경도", example = "126.93754073973317")
        Double longitude,
        @Schema(description = "이미지 url", example = "https://blog.kakaocdn.net/dn/3ghE9/btq2RjJq0zF/skhqeIUWQca9a8VDiPW1Sk/img.png")
        String imageUrl,
        @Schema(description = "생성 시간", example = "2023-02-27T20:44:28")
        LocalDateTime createdDate,
        @Schema(description = "수정 시간", example = "2023-02-27T20:44:28")
        LocalDateTime modifiedDate
) {
}
