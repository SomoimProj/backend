package com.oinzo.somoim.domain.boardlike.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.BoardLikeResponse;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.boardlike.entity.BoardLike;
import com.oinzo.somoim.domain.boardlike.repository.BoardLikeRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardLikeService {
    private final ClubBoardRepository clubBoardRepository;
    private final BoardLikeRepository likeRepository;
    private final ClubUserRepository clubUserRepository;

    public BoardLikeResponse addLike(Long userId, Long boardId) {
        ClubBoard board = clubBoardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_BOARD));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId, board.getClub().getId())) {
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        }
        if (likeRepository.existsByBoard_IdAndUserId(boardId, userId)) {
            throw new BaseException(ErrorCode.ALREADY_LIKED);
        }

        BoardLike like = BoardLike.builder()
                .userId(userId)
                .board(board)
                .build();
        return BoardLikeResponse.from(likeRepository.save(like));
    }

    public void deleteLike(Long userId, Long boardId) {
        if (!clubBoardRepository.existsById(boardId)) {
            throw new BaseException(ErrorCode.WRONG_BOARD);
        }
        BoardLike like = likeRepository.findByBoard_IdAndUserId(boardId, userId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_LIKE));
        likeRepository.delete(like);
    }

    public List<BoardLikeResponse> readAllLike(Long boardId) {
        if (!clubBoardRepository.existsById(boardId)) {
            throw new BaseException(ErrorCode.WRONG_BOARD);
        }
        List<BoardLike> like = likeRepository.findAllByBoard_Id(boardId);
        return BoardLikeResponse.likeListToResponseList(like);
    }
}