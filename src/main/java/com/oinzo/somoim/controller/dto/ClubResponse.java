package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ClubResponse {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String area;
    private int memberLimit;
    private int memberCnt;
    private Favorite favorite;

    public static ClubResponse from(Club club){
        return ClubResponse.builder()
                .id(club.getId())
                .name(club.getName())
                .description(club.getDescription())
                .imageUrl(club.getImageUrl())
                .area(club.getArea())
                .memberLimit(club.getMemberLimit())
                .memberCnt(club.getMemberCnt())
                .build();
    }

    public static List<ClubResponse> listToBoardResponse(List<Club> clubList){
        return clubList.stream()
                .map(ClubResponse::from)
                .collect(Collectors.toList());
    }

}