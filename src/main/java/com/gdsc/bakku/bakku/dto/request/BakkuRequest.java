package com.gdsc.bakku.bakku.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BakkuRequest {
    @NotBlank
    private String comment;

    @NotNull
    private int cleanWeight;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate decorateDate;

    @NotNull
    private MultipartFile titleImage;

    @NotNull
    private MultipartFile afterImage;

    private MultipartFile beforeImage;

    @NotBlank
    private String groupName;

    @NotNull
    private Long oceanId;
}
