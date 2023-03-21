package com.oinzo.somoim.domain.club.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.dto.ClubCreateRequest;
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
    @Enumerated(value = EnumType.STRING)
    private Favorite favorite;

    private int viewCnt;

    public Club setCnt(int cnt) {
        this.viewCnt = cnt;
        return this;
    }

    public static Club from(ClubCreateRequest clubCreateRequest){
        return Club.builder()
                .name(clubCreateRequest.getName())
                .description(clubCreateRequest.getDescription())
                .imageUrl(clubCreateRequest.getImageUrl())
                .area(clubCreateRequest.getArea())
                .memberLimit(clubCreateRequest.getMemberLimit())
                .memberCnt(0)
                .favorite(clubCreateRequest.getFavoriteType())
                .viewCnt(0)
                .build();
    }

}
