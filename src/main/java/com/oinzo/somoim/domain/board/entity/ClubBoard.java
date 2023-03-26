package com.oinzo.somoim.domain.board.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.common.type.Category;
import com.oinzo.somoim.controller.dto.BoardCreateRequest;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @ManyToOne
    @JoinColumn
    private Club club;

    @ManyToOne
    @JoinColumn
    private User user;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Category category;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String imageUrl;

    public static ClubBoard from(BoardCreateRequest boardRequest, Club club, User user){
        return ClubBoard.builder()
                .club(club)
                .user(user)
                .category(boardRequest.getCategoryType())
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .imageUrl(boardRequest.getImageUrl())
                .build();
    }

    public void updateClubBoard(BoardCreateRequest boardCreateRequest){
        this.category = boardCreateRequest.getCategoryType();
        this.title = boardCreateRequest.getTitle();
        this.content = boardCreateRequest.getContent();
        this.imageUrl = boardCreateRequest.getImageUrl();
    }
}
