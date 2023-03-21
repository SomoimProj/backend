package com.oinzo.somoim.domain.board.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.BoardRequest;
import com.oinzo.somoim.domain.board.entity.Board;
import com.oinzo.somoim.domain.board.repository.BoardRepository;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.club.service.ClubService;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ClubRepository clubRepository;

    private final UserRepository userRepository;

    public Board addBoard(BoardRequest request){
        try{
            clubRepository.findById(request.getClubId()).orElseThrow(()->new BaseException(ErrorCode.WRONG_CLUB));
//            userRepository.findById(request.getUserId()).orElseThrow(()->new BaseException(ErrorCode.USER_NOT_FOUND));
            return boardRepository.save(Board.from(request));
        }catch (IllegalArgumentException e){
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    };
}
