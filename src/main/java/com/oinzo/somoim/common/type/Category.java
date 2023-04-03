package com.oinzo.somoim.common.type;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;

public enum Category {

    FREE,
    FAVORITE,
    MEET,
    JOIN,
    ANNOUNCEMENT;

    public static Category valueOfOrHandleException(String string) {
        try {
            return Category.valueOf(string);
        } catch (IllegalArgumentException e)  {
            throw new BaseException(ErrorCode.WRONG_CATEGORY, "category=" + string);
        }
    }

}
