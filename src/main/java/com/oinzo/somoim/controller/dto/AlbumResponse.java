package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public static AlbumResponse from(ClubAlbum album){
        return AlbumResponse.builder()
                .id(album.getId())
                .userId(album.getUser().getId())
                .userName(album.getUser().getName())
                .userImg(album.getUser().getProfileUrl())
                .clubId(album.getClubId())
                .imageUrl(album.getImageUrl())
                .createdAt(album.getCreatedAt())
                .updatedAt(album.getUpdatedAt())
                .build();
    }

    public static List<AlbumResponse> listToAlbumResponse(List<ClubAlbum> albumList){
        return albumList.stream()
                .map(AlbumResponse::from)
                .collect(Collectors.toList());
    }
}