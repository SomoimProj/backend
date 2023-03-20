package com.oinzo.somoim.domain.club.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ClubRequestDto {
    private String name;

    public ClubRequestDto setName(String name) {
        this.name = name;
        return this;
    }

    public ClubRequestDto setArea(String area) {
        this.area = area;
        return this;
    }

    public ClubRequestDto setFavorite(String favorite) {
        this.favorite = favorite;
        return this;
    }

    private String description;
    private String imageUrl;
    private String area;
    private int memberLimit;
    private int memberCnt;
    private String favorite;
    private int cnt;


}
