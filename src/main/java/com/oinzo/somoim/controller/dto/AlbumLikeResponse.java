package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.albumlike.entity.AlbumLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AlbumLikeResponse {
    private Long id;
    private Long userId;
    private Long albumId;

    public static AlbumLikeResponse from(AlbumLike like){
        return AlbumLikeResponse.builder()
                .id(like.getId())
                .userId(like.getUserId())
                .albumId(like.getAlbum().getId())
                .build();
    }
}
