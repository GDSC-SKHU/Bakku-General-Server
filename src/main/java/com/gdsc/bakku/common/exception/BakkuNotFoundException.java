package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class BakkuNotFoundException extends CustomAbstractException{
    public BakkuNotFoundException() {
        super(StatusEnum.BAKKU_NOT_FOUND);
    }
}
