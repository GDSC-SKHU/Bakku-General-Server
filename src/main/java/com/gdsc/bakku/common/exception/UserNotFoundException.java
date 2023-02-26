package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class UserNotFoundException extends CustomAbstractException {
    public UserNotFoundException() {
        super(StatusEnum.EVENT_NOT_FOUND);
    }
}
