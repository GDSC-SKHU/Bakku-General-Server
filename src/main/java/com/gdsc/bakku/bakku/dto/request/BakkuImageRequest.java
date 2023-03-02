package com.gdsc.bakku.bakku.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BakkuImageRequest {
    private MultipartFile titleImage;

    private MultipartFile afterImage;

    private MultipartFile beforeImage;

    @NotNull
    private Boolean isChangeTitle;

    @NotNull
    private Boolean isChangeAfter;

    @NotNull
    private Boolean isChangeBefore;
}
