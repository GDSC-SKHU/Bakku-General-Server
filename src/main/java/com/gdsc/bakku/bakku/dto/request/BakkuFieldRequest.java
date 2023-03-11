package com.gdsc.bakku.bakku.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "application/json 전용 바꾸 필드 Request body(파일 제외)")
public class BakkuFieldRequest {
    @NotBlank
    @Schema(description = "그룹 이름", example = "GDSC")
    private String groupName;

    @NotNull
    @Schema(description = "바다 FK", example = "1")
    private Long oceanId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "청소한 날짜", example = "2023-05-11")
    private LocalDate decorateDate;

    @NotNull
    @Max(10000)
    @Schema(description = "청소한 무게", example = "100")
    private int cleanWeight;

    @NotBlank
    @Schema(description = "내용", example = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
    private String comment;
}
