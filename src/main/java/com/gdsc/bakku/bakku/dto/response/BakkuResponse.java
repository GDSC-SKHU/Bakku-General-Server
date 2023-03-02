package com.gdsc.bakku.bakku.dto.response;

import lombok.*;

import java.time.LocalDate;

@Builder
public record BakkuResponse(
        Long id,
        String comment,
        int cleanWeight,
        LocalDate decorateTime,
        String titleImageUrl,
        String beforeImageUrl,
        String afterImageUrl,
        String groupName,
        Long oceanId,
        String oceanName
) {
}
