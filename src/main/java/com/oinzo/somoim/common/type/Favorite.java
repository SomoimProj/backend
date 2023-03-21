package com.oinzo.somoim.common.type;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;

public enum Favorite {
    SPORTS,
    GAME,
    TRIP;

    public static Favorite valueOfOrHandleException(String string) {
        try {
            return Favorite.valueOf(string);
        } catch (IllegalArgumentException e)  {
            throw new BaseException(ErrorCode.WRONG_FAVORITE, "favorite=" + string);
        }
    }

}
