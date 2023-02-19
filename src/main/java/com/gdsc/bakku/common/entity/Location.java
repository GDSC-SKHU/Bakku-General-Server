package com.gdsc.bakku.common.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Embeddable
public class Location {

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;
}
