package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class AlbumResponse {

    private Long id;
    private Long userId;
    private Long clubId;
    private String imageUrl;
    public static AlbumResponse from(ClubAlbum album){
        return AlbumResponse.builder()
                .id(album.getId())
                .clubId(album.getClub().getId())
                .userId(album.getUser().getId())
                .imageUrl(album.getImageUrl())
                .build();
    }

    public static List<AlbumResponse> listToAlbumResponse(List<ClubAlbum> albumList){
        return albumList.stream()
                .map(AlbumResponse::from)
                .collect(Collectors.toList());
    }
}