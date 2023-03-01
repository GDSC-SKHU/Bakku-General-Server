package com.gdsc.bakku.common.exception;

import com.gdsc.bakku.common.error.StatusEnum;

public class GroupNotFoundException extends CustomAbstractException {
    public GroupNotFoundException() {
        super(StatusEnum.GROUP_NOT_FOUND);
    }
}
