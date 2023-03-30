package com.oinzo.somoim.domain.boardlike.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class BoardLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long boardId;

    @ManyToOne
    @JoinColumn
    private User user;

    public static BoardLike from(User user,Long boardId) {
        return BoardLike.builder()
                .user(user)
                .boardId(boardId)
                .build();
    }
}
