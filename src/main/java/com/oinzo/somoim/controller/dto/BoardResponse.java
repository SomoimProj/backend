package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.board.entity.ClubBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class BoardResponse {

    private Long id;
    private Long userId;
    private String userImg;
    private String userName;
    private String title;
    private String content;
    private String imageUrl;
    private String category;
    private int likeCnt;
    private int commentCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardResponse from(ClubBoard board, int likeCnt,int commentCnt) {
        return BoardResponse.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .userName(board.getUser().getName())
                .userImg(board.getUser().getProfileUrl())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .category(board.getCategory().name())
                .likeCnt(likeCnt)
                .commentCnt(commentCnt)
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

}
