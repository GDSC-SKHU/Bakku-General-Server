package com.gdsc.bakku.bakku.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GroupResponse(
        Long id,
        String groupName,
        Double totalWeight,
        Double totalCount,
        LocalDate createdDate
) {
}
