package com.gdsc.bakku.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum StatusEnum {

    //400
    NOT_SUPPORT_FILE_FORMANT(BAD_REQUEST, "파일 형식은 이미지만 지원합니다."),

    //404
    OCEAN_NOT_FOUND(NOT_FOUND, "해당 ID를 가진 바다 없음"),
    EVENT_NOT_FOUND(NOT_FOUND, "해당 ID를 가진 행사 없음"),
    BAKKU_NOT_FOUND(NOT_FOUND, "해당 ID를 가진 바꾸 없음"),
    GCS_FILE_NOT_FOUND(NOT_FOUND, "GCS에 해당 파일(%s)이 존재하지 않습니다."),

    //500
    COULD_NOT_GENERATE_INPUT_STREAM(INTERNAL_SERVER_ERROR, "입력 스트림을 생성하는 과정에서 문제가 생겼습니다.");

    private final HttpStatus httpStatus;

    private final String detail;

    StatusEnum(HttpStatus httpStatus, String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }
}
