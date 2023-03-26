package com.oinzo.somoim.common.type;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;

public enum Category {

    자유,
    관심사,
    정모,
    가입,
    공지;

    public static Category valueOfOrHandleException(String string) {
        try {
            return Category.valueOf(string);
        } catch (IllegalArgumentException e)  {
            throw new BaseException(ErrorCode.WRONG_CATEGORY, "category=" + string);
        }
    }

}
