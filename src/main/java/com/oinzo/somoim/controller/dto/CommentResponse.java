package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.boardcomment.entity.BoardComment;
import com.oinzo.somoim.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private Long boardId;
    private Long userId;
    private String comment;
    private String userName;
    private String profileImg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static CommentResponse from(BoardComment comment, User user){
        return CommentResponse.builder()
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .userId(comment.getUser().getId())
                .comment(comment.getComment())
                .userName(user.getName())
                .profileImg(user.getProfileUrl())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}