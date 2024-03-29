package com.gdsc.bakku.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum StatusEnum {
    //400
    NOT_SUPPORT_FILE_FORMANT(BAD_REQUEST, "파일 형식은 이미지만 지원합니다."),
    INVALID_POSITION(BAD_REQUEST, "위도(-90 ~ 90) 혹은 경도(-180 ~ 180)의 범위를 확인해 주세요"),
    ALREADY_REPORT(BAD_REQUEST,"이미 신고하셨습니다"),
    FILE_SIZE_EXCEEDED(BAD_REQUEST, "업로드 한 파일 용량 초과(10MB 이하로 요청해 주세요.)"),

    //403
    USER_NO_PERMISSION(FORBIDDEN, "해당 유저는 데이터를 접근할 권한이 없습니다."),

    //404
    OCEAN_NOT_FOUND(NOT_FOUND, "해당 ID를 가진 바다 없음"),
    EVENT_NOT_FOUND(NOT_FOUND, "해당 ID를 가진 행사 없음"),
    GROUP_NOT_FOUND(NOT_FOUND, "해당 이름을 가진 그룹이 없음"),
    BAKKU_NOT_FOUND(NOT_FOUND, "해당 ID를 가진 바꾸 없음"),
    GCS_FILE_NOT_FOUND(NOT_FOUND, "GCS에 해당 파일(%s)이 존재하지 않습니다."),
    USER_NOT_FOUND(NOT_FOUND, "해당 ID를 가진 USER 없음"),

    //500
    COULD_NOT_GENERATE_INPUT_STREAM(INTERNAL_SERVER_ERROR, "입력 스트림을 생성하는 과정에서 문제가 생겼습니다.");

    private final HttpStatus httpStatus;

    private final String detail;

    StatusEnum(HttpStatus httpStatus, String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }
}
