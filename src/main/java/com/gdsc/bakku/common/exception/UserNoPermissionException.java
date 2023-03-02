package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class UserNoPermissionException extends CustomAbstractException {
    public UserNoPermissionException() {
        super(StatusEnum.USER_NO_PERMISSION);
    }
}
