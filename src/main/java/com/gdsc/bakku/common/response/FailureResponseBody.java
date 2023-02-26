package com.gdsc.bakku.common.response;

import com.gdsc.bakku.common.error.StatusEnum;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class FailureResponseBody{

    private final String code;

    private final String detail;


    public static ResponseEntity<FailureResponseBody> toResponseEntity(StatusEnum statusEnum) {
        return ResponseEntity
                .status(statusEnum.getHttpStatus())
                .body(FailureResponseBody.builder()
                        .detail(statusEnum.getDetail())
                        .code(statusEnum.getHttpStatus().name())
                        .build()
                );
    }

}
