package com.gdsc.bakku.bakku.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BakkuFieldRequest {
    private String groupName;

    private Long oceanId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate decorateDate;

    private int cleanWeight;

    private String comment;
}
