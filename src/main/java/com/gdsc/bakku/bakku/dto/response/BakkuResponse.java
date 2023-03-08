package com.gdsc.bakku.bakku.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Builder
@Schema(description = "바꾸 응답입니다.")
public record BakkuResponse(
        @Schema(description = "PK", example = "1")
        Long id,
        @Schema(description = "내용", example = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
        String comment,
        @Schema(description = "청소한 무게(kg)", example = "10")
        int cleanWeight,
        @Schema(description = "청소한 시간", example = "2023-02-27T20:44:28")
        LocalDate decorateTime,
        @Schema(description = "메인 이미지 url", example = "https://i.namu.wiki/i/TwwcGwWqzN8HWluj-kUbX4rzuhHristP1LsE3K_gG_vUfMoFiqPRT0_Nm5LPjySVzrvh5NsyDxVzV9sRpFec5iNswOqUjEYt2qmcGn7xtM44DwD_A28ma3iJD69Hnr4Ctfgcu6C7tN5Vo4-voblIWg.webp")
        String titleImageUrl,
        @Schema(description = "청소하기 전 이미지 url", example = "https://i.namu.wiki/i/2bcOtfN4kQbdKULZGz1jj7O0gjqepUCaoQIBGxD60MElFle7IWR0n5AE4wS1BQkj_8c3ekBK4LTF1_t4uGspaw6jUkU2_yBMHtjHiIgTwT8zu5eIM6BwPmET88jFTA9ssIKtvBHkymPvq5juZEjlOQ.webp")
        String beforeImageUrl,
        @Schema(description = "청소한 후 이미지 url", example = "https://i.namu.wiki/i/s3-pNa7-o53PhuiSEdj84c50npPKWV8YxPdCHsapQdvcfk7nNQqV62v5d6sCOx4awXepOMoIdf-uxLqpZB_HO3jkCDHyb64SLJKCz-JYh-iP6kBFboE66spMGV2R8q1UVjk2rZO8NXGQBorzxycTLA.webp")
        String afterImageUrl,
        @Schema(description = "청소한 단체 이름", example = "GDSC")
        String groupName,
        @Schema(description = "청소한 바다 FK", example = "1")
        Long oceanId,
        @Schema(description = "바다 이름", example = "을왕리 해수욕장")
        String oceanName
) {
}
