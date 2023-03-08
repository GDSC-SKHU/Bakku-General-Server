package com.gdsc.bakku.bakku.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Data
@Schema(description = "multipart/form-data 전용 이미지들 바꾸 request body입니다.(필드 제외)")
public class BakkuImageRequest {
    @Schema(description = "메인 이미지", implementation = Object.class)
    private MultipartFile titleImage;

    @Schema(description = "청소 후 이미지")
    private MultipartFile afterImage;

    @Schema(description = "청소 전 이미지")
    private MultipartFile beforeImage;

    @NotNull
    @Schema(description = "메인 이미지를 바꾸는지에 대한 여부")
    private Boolean isChangeTitle;

    @NotNull
    @Schema(description = "청소 후 이미지 바꾸는지에 대한 여부")
    private Boolean isChangeAfter;

    @NotNull
    @Schema(description = "청소 전 이미지 바꾸는지에 대한 여부")
    private Boolean isChangeBefore;
}
