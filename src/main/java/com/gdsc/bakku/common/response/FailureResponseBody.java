package com.gdsc.bakku.common.response;

import com.gdsc.bakku.common.error.StatusEnum;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class FailureResponseBody extends AbstractResponseBody{

    @Builder
    public FailureResponseBody( String statusDetail, String code) {
        super(statusDetail, code);
    }

    public static ResponseEntity<FailureResponseBody> toResponseEntity(StatusEnum statusEnum) {
        return ResponseEntity
                .status(statusEnum.getHttpStatus())
                .body(FailureResponseBody.builder()
                        .statusDetail(statusEnum.getHttpStatus()
                                .name())
                        .code(statusEnum.getDetail())
                        .build()
                );
    }

}
