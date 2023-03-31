package com.oinzo.somoim.domain.boardlike.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ClubBoard board;
    @NotNull
    private Long userId;

    public static BoardLike from(Long userId,ClubBoard board) {
        return BoardLike.builder()
                .userId(userId)
                .board(board)
                .build();
    }
}