package com.oinzo.somoim.common.type;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;

public enum Favorite {
    GAME,
    OUTDOOR,
    EXERCISE,
    HUMANITIES,
    FOREIGN,
    CULTURE,
    MUSIC,
    CRAFTS,
    DANCE,
    VOLUNTEER,
    SOCIETY,
    CAR,
    PICTURE,
    BASEBALL,
    COOK,
    PET,
    FAMILY,
    FREE;

    public static Favorite valueOfOrHandleException(String string) {
        try {
            String treatedString = string.trim().toUpperCase();
            return Favorite.valueOf(treatedString);
        } catch (IllegalArgumentException e)  {
            throw new BaseException(ErrorCode.WRONG_FAVORITE, "favorite=" + string);
        }
    }

}

