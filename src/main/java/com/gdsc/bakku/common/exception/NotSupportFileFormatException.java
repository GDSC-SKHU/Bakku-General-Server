package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class NotSupportFileFormatException extends CustomAbstractException{
    public NotSupportFileFormatException() {
        super(StatusEnum.NOT_SUPPORT_FILE_FORMANT);
    }
}
