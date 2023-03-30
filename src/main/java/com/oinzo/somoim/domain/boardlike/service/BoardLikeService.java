package com.oinzo.somoim.domain.boardlike.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.BoardLikeResponse;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.boardlike.entity.BoardLike;
import com.oinzo.somoim.domain.boardlike.repository.BoardLikeRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardLikeService {
    private final UserRepository userRepository;
    private final ClubBoardRepository clubBoardRepository;
    private final BoardLikeRepository likeRepository;
    private final ClubUserRepository clubUserRepository;

    public BoardLikeResponse addLike(Long userId, Long boardId){
        if(likeRepository.existsByBoardIdAndUserId(boardId,userId))
            throw new BaseException(ErrorCode.ALREADY_LIKED);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
        ClubBoard board = clubBoardRepository.findById(boardId)
                .orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId,board.getClub().getId()))
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        BoardLike like = likeRepository.save(BoardLike.from(user,boardId));
        return BoardLikeResponse.from(like);
    }

    public List<BoardLikeResponse> readAllLike(Long boardId){
        if(!clubBoardRepository.existsById(boardId))
            throw new BaseException(ErrorCode.WRONG_BOARD);
        List<BoardLike> likeLists = likeRepository.findAllByBoardId(boardId);
        return BoardLikeResponse.responseToList(likeLists);
    }

    public void deleteLike(Long userId,Long boardId){
        if(!userRepository.existsById(userId))
            throw  new BaseException(ErrorCode.USER_NOT_FOUND);
        ClubBoard board = clubBoardRepository.findById(boardId)
                .orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId,board.getClub().getId())) {
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        }
        BoardLike like = likeRepository.findByBoardIdAndUserId(boardId,userId)
                .orElseThrow(()-> new BaseException(ErrorCode.WRONG_LIKE));
        likeRepository.delete(like);
    }
}