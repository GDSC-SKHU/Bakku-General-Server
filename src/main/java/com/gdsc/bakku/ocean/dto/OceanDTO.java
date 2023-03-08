package com.gdsc.bakku.ocean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "바다의 정보들을 가지는 데이터 형식입니다.")
public record OceanDTO(
    @Schema(description = "PK", example = "1")
    Long id,
    @Schema(description = "이름", example = "바다 이름")
    String name,
    @Schema(description = "주소", example = "경기도 김포시")
    String address,
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
