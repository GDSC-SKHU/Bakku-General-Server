package com.gdsc.bakku.bakku.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GroupResponse(
        Long groupId,
        String groupName,
        Double totalWeight,
        Double totalCount,
        LocalDate createdDate
) {
}
