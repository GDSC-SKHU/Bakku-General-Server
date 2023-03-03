package com.gdsc.bakku.bakku.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupRankingResponse(
        List<GroupResponse> groupWeight,
        List<GroupResponse> groupCount
) {
}
