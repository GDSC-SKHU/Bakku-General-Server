package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class AlreadyReportException extends CustomAbstractException {
    public AlreadyReportException() {
        super(StatusEnum.ALREADY_REPORT);
    }
}
