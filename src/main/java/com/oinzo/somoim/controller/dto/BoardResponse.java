package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardResponse from(ClubBoard board){
        return BoardResponse.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .userName(board.getUser().getName())
                .userImg(board.getUser().getProfileUrl())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .category(board.getCategory().name())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    public static List<BoardResponse> ListToBoardResponse(List<ClubBoard> boardList){
        return boardList.stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
    }

}
