package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class EventNotFoundException extends CustomAbstractException {
    public EventNotFoundException() {
        super(StatusEnum.EVENT_NOT_FOUND);
    }
}
