package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class CouldNotGenerateInputStreamException extends CustomAbstractException{
    public CouldNotGenerateInputStreamException() {
        super(StatusEnum.COULD_NOT_GENERATE_INPUT_STREAM);
    }
}
