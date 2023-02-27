package com.gdsc.bakku.event.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventDTO(
        Long id,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String comment,
        Double latitude,
        Double longitude,
        String imageUrl,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
) {
}
