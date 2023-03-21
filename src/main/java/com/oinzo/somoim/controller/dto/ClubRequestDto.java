package com.oinzo.somoim.controller.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubRequestDto {

    private String name;
    private String description;
    private String imageUrl;
    private String area;
    private int memberLimit;
    private int memberCnt;
    private String favorite;
    private int viewCnt;

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
}
