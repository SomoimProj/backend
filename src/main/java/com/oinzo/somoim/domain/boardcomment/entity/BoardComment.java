package com.oinzo.somoim.domain.boardcomment.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.controller.dto.CommentRequest;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class BoardComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ClubBoard board;

    @NotBlank
    private String comment;

    public static BoardComment from(CommentRequest request, ClubBoard board, User user){
        return BoardComment.builder()
                .user(user)
                .board(board)
                .comment(request.getComment())
                .build();
    }
    public void updateComment(CommentRequest request){
        this.comment = request.getComment();
    }
}