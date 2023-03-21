package com.oinzo.somoim.domain.club.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.domain.club.dto.ClubCreateDto;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    private String imageUrl;
    @NotNull
    private String area;
    @NotNull
    private int memberLimit;
    private int memberCnt;
    @NotNull
    private String favorite;

    public Club setCnt(int cnt) {
        this.viewCnt = cnt;
        return this;
    }

    private int viewCnt;

    public static Club from(ClubCreateDto clubCreateDto){
        return Club.builder()
                .name(clubCreateDto.getName())
                .description(clubCreateDto.getDescription())
                .imageUrl(clubCreateDto.getImageUrl())
                .area(clubCreateDto.getArea())
                .memberLimit(clubCreateDto.getMemberLimit())
                .memberCnt(0)
                .favorite(clubCreateDto.getFavorite())
                .viewCnt(0)
                .build();
    }

}