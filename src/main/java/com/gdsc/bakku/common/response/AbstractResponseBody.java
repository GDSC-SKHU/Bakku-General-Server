package com.gdsc.bakku.common.response;

import lombok.Getter;

@Getter
public abstract class AbstractResponseBody {
    private final String code;

    private final String detail;

    public AbstractResponseBody(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }
}
