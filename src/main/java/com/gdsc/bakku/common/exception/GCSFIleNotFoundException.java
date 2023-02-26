package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class GCSFIleNotFoundException extends CustomAbstractException{
    public GCSFIleNotFoundException() {
        super(StatusEnum.GCS_FILE_NOT_FOUND);
    }
}
