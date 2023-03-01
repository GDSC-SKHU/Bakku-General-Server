package com.gdsc.bakku.bakku.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BakkuFieldRequest {
    @NotBlank
    private String groupName;

    @NotNull
    private Long oceanId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate decorateDate;

    @NotNull
    private int cleanWeight;

    @NotBlank
    private String comment;
}
