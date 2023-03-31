package com.oinzo.somoim.domain.boardlike.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.BoardLikeResponse;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.boardlike.entity.BoardLike;
import com.oinzo.somoim.domain.boardlike.repository.BoardLikeRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardLikeService {
    private final UserRepository userRepository;
    private final ClubBoardRepository clubBoardRepository;
    private final BoardLikeRepository likeRepository;
    private final ClubUserRepository clubUserRepository;

    public BoardLikeResponse addLike(Long userId, Long boardId){
        if(!userRepository.existsById(userId))
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        ClubBoard board = clubBoardRepository.findById(boardId)
                .orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId,board.getClub().getId()))
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        if(likeRepository.existsByBoardIdAndUserId(boardId,userId))
            throw new BaseException(ErrorCode.ALREADY_LIKED);
        BoardLike like = likeRepository.save(BoardLike.from(userId,board));
        return BoardLikeResponse.from(like);
    }

    public void deleteLike(Long userId,Long boardId){
        if(!userRepository.existsById(userId))
            throw  new BaseException(ErrorCode.USER_NOT_FOUND);
        if(!clubBoardRepository.existsById(boardId))
            throw new BaseException(ErrorCode.WRONG_BOARD);
        BoardLike like = likeRepository.findByBoardIdAndUserId(boardId,userId)
                .orElseThrow(()-> new BaseException(ErrorCode.WRONG_LIKE));
        likeRepository.delete(like);
    }
}