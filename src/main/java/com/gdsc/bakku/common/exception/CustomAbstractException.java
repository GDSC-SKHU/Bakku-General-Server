package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class CustomAbstractException extends RuntimeException {
    protected final StatusEnum statusEnum;
}
