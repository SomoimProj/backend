package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.boardlike.entity.BoardLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class BoardLikeResponse {
    private Long id;
    private Long userId;
    private Long boardId;

    public static BoardLikeResponse from(BoardLike like) {
        return BoardLikeResponse.builder()
                .id(like.getId())
                .userId(like.getUserId())
                .boardId(like.getBoard().getId())
                .build();
    }

    public static List<BoardLikeResponse> likeListToResponseList(List<BoardLike> likes) {
        return likes.stream()
                .map(BoardLikeResponse::from)
                .collect(Collectors.toList());
    }

}
