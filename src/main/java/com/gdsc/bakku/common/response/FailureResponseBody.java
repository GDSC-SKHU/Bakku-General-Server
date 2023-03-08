package com.gdsc.bakku.common.response;

import com.gdsc.bakku.common.error.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@Schema(description = "Http status가 400-500일때 반환되는 응답입니다.")
public class FailureResponseBody{

    @Schema(description = "오류 코드", example = "CANT_SLEEP")
    private final String code;

    @Schema(description = "상세 메세지(상황에 따라 리스트 또는 문자열로 반환됨)", example = "오류다 오류")
    private final Object detail;


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
