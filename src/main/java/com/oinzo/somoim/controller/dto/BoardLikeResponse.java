package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.boardlike.entity.BoardLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class BoardLikeResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userImg;
    private Long boardId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardLikeResponse from(BoardLike like){
        return BoardLikeResponse.builder()
                .id(like.getId())
                .userId(like.getUser().getId())
                .userName(like.getUser().getName())
                .userImg(like.getUser().getProfileUrl())
                .boardId(like.getBoardId())
                .createdAt(like.getCreatedAt())
                .updatedAt(like.getUpdatedAt())
                .build();
    }

    public static List<BoardLikeResponse> responseToList(List<BoardLike> like){
        return like.stream()
                .map(BoardLikeResponse::from)
                .collect(Collectors.toList());
    }
}
