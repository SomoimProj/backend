package com.oinzo.somoim.domain.board.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.controller.dto.BoardRequest;
import com.oinzo.somoim.domain.board.dto.BoardCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ClubBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long clubId;
    @NotNull
    private Long userId;
    private String category;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private String imageUrl;

    public static ClubBoard from(BoardCreateRequest boardRequest,Long clubId, Long userId){
        return ClubBoard.builder()
                .clubId(clubId)
                .userId(userId)
                .category(boardRequest.getCategory())
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .imageUrl(boardRequest.getImageUrl())
                .build();
    }
}
