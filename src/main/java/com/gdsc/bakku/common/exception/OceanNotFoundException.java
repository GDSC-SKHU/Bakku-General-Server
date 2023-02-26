package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class OceanNotFoundException extends CustomAbstractException{
    public OceanNotFoundException() {
        super(StatusEnum.OCEAN_NOT_FOUND);
    }
}
