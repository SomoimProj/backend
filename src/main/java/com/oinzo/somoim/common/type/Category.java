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
            String tradedString = string.trim().toUpperCase();
            return Category.valueOf(tradedString);
        } catch (IllegalArgumentException e)  {
            throw new BaseException(ErrorCode.WRONG_CATEGORY, "category=" + string);
        }
    }

}
