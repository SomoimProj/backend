package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
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
    private Long memberCnt;
    private Favorite favorite;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ClubResponse fromClubAndMemberCnt(Club club, Long memberCnt){
        return ClubResponse.builder()
                .id(club.getId())
                .name(club.getName())
                .description(club.getDescription())
                .imageUrl(club.getImageUrl())
                .area(club.getArea())
                .memberLimit(club.getMemberLimit())
                .memberCnt(memberCnt)
                .favorite(club.getFavorite())
                .createdAt(club.getCreatedAt())
                .updatedAt(club.getUpdatedAt())
                .build();
    }
}
