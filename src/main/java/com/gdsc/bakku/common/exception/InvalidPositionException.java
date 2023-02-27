package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class InvalidPositionException extends CustomAbstractException {
    public InvalidPositionException() {
        super(StatusEnum.INVALID_POSITION);
    }
}
