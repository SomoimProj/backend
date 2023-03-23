package com.oinzo.somoim.domain.board.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.board.dto.BoardCreateRequest;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClubBoardService {

    private final ClubBoardRepository clubBoardRepository;
    private final ClubRepository clubRepository;

    private final UserRepository userRepository;

    public ClubBoard addBoard(BoardCreateRequest request, Long clubId,Long userId){
        try{
            userRepository.findById(userId).orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
            clubRepository.findById(clubId).orElseThrow(()->new BaseException(ErrorCode.WRONG_CLUB));
            return clubBoardRepository.save(ClubBoard.from(request,clubId,userId));
        }catch (IllegalArgumentException e){
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ClubBoard> clubBoardList(Long clubId){
        try{
            clubRepository.findById(clubId).orElseThrow(()->new BaseException(ErrorCode.WRONG_CLUB));
            return clubBoardRepository.findAllByClubIdIs(clubId);
        }catch (IllegalArgumentException e){
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public ClubBoard readBoard(Long boardId){
        try {
            return clubBoardRepository.findById(boardId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));

        }catch (IllegalArgumentException e){
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public ClubBoard updateBoard(Long boardId,BoardCreateRequest request,Long userId){
        try {
            ClubBoard board = clubBoardRepository.findById(boardId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
            userRepository.findById(userId).orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
            if(!userId.equals(board.getUserId())) throw new BaseException(ErrorCode.FORBIDDEN_REQUEST);
            board.updateClubBoard(request);
            return clubBoardRepository.save(board);

        }catch (IllegalArgumentException e){
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteBoard(Long boardId,Long userId){
        try {
            ClubBoard board = clubBoardRepository.findById(boardId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
            userRepository.findById(userId).orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
            if(!userId.equals(board.getUserId())) throw new BaseException(ErrorCode.FORBIDDEN_REQUEST);

        }catch (IllegalArgumentException e){
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
