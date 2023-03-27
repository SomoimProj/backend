package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.board.entity.ClubBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String category;

    public static BoardResponse from(ClubBoard board){
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .category(board.getCategory().name())
                .build();
    }

    public static List<BoardResponse> ListToBoardResponse(List<ClubBoard> boardList){
        return boardList.stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
    }

}
