package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AlbumResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String userImg;
    private Long clubId;
    private String imageUrl;
    private int likeCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public static AlbumResponse from(ClubAlbum album,int likeCnt){
        return AlbumResponse.builder()
                .id(album.getId())
                .userId(album.getUser().getId())
                .userName(album.getUser().getName())
                .userImg(album.getUser().getProfileUrl())
                .clubId(album.getClubId())
                .imageUrl(album.getImageUrl())
                .likeCnt(likeCnt)
                .createdAt(album.getCreatedAt())
                .updatedAt(album.getUpdatedAt())
                .build();
    }

}