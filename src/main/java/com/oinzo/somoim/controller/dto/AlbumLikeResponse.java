package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.albumlike.entity.AlbumLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class AlbumLikeResponse {
    private Long id;
    private Long userId;
    private Long albumId;

    public static AlbumLikeResponse from(AlbumLike like) {
        return AlbumLikeResponse.builder()
                .id(like.getId())
                .userId(like.getUserId())
                .albumId(like.getAlbum().getId())
                .build();
    }

    public static List<AlbumLikeResponse> albumLikeToList(List<AlbumLike> likes) {
        return likes.stream()
                .map(AlbumLikeResponse::from)
                .collect(Collectors.toList());
    }
}
