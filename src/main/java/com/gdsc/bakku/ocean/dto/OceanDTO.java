package com.gdsc.bakku.ocean.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OceanDTO(
    Long id,
    String name,
    String address,
    Double latitude,
    Double longitude,
    String imageUrl,
    LocalDateTime createdDate,
    LocalDateTime modifiedDate
) {
}
