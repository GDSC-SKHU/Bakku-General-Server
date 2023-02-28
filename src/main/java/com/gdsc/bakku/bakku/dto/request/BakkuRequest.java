package com.gdsc.bakku.bakku.dto.request;

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
    private String comment;

    private int cleanWeight;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate decorateDate;

    private MultipartFile titleImage;

    private MultipartFile afterImage;

    private MultipartFile beforeImage;

    private String groupName;

    private Long oceanId;
}
