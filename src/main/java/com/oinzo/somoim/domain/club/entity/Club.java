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
    @NotNull
    private String imageUrl;
    @NotNull
    private String area;
    @NotNull
    private int memberLimit;
    private int memberCnt;
    @NotNull
    private String favorite;
    private int cnt;

    public static Club from(Club club){
        return Club.builder()
                .name(club.getName())
                .description(club.getDescription())
                .imageUrl(club.getImageUrl())
                .area(club.getArea())
                .memberLimit(club.getMemberLimit())
                .favorite(club.getFavorite())
                .build();
    }
}