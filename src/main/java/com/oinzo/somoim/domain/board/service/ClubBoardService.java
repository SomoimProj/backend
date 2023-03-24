package com.oinzo.somoim.domain.board.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Category;
import com.oinzo.somoim.domain.board.dto.BoardCreateRequest;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ClubBoardService {
    private final ClubBoardRepository clubBoardRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    @Transactional
    public ClubBoard addBoard(BoardCreateRequest request, Long clubId,Long userId){
        userRepository.findById(userId).orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
        clubRepository.findById(clubId).orElseThrow(()->new BaseException(ErrorCode.WRONG_CLUB));
        return clubBoardRepository.save(ClubBoard.from(request,clubId,userId));
    }

    public List<ClubBoard> clubBoardList(Long clubId,Pageable pageable){
        clubRepository.findById(clubId).orElseThrow(()->new BaseException(ErrorCode.WRONG_CLUB));
        return clubBoardRepository.findAllByClubIdIs(clubId,pageable).getContent();
    }

    public List<ClubBoard> allClubBoardList(Long clubId){
        clubRepository.findById(clubId).orElseThrow(()->new BaseException(ErrorCode.WRONG_CLUB));
        Pageable pageable = Pageable.ofSize(clubBoardRepository.findAll().size());
        return clubBoardRepository.findAllByClubIdIs(clubId,pageable).getContent();
    }

    public List<ClubBoard> clubBoardListByCategory(Long clubId,String category,Pageable pageable){
        Category newCategory = Category.valueOfOrHandleException(category);
        clubRepository.findById(clubId).orElseThrow(()->new BaseException(ErrorCode.WRONG_CLUB));
        return clubBoardRepository.findAllByClubIdIsAndCategory(clubId,newCategory,pageable).getContent();
    }

    public List<ClubBoard> allClubBoardListByCategory(Long clubId,String category){
        Category newCategory = Category.valueOfOrHandleException(category);
        clubRepository.findById(clubId).orElseThrow(()->new BaseException(ErrorCode.WRONG_CLUB));
        Pageable pageable = Pageable.ofSize(clubBoardRepository.findAll().size());
        return clubBoardRepository.findAllByClubIdIsAndCategory(clubId,newCategory,pageable).getContent();
    }

    public ClubBoard readBoard(Long boardId){
        return clubBoardRepository.findById(boardId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
    }

    @Transactional
    public ClubBoard updateBoard(Long boardId,BoardCreateRequest request,Long userId){
        ClubBoard board = clubBoardRepository.findById(boardId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
        userRepository.findById(userId).orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
        if(!userId.equals(board.getUserId())) throw new BaseException(ErrorCode.FORBIDDEN_REQUEST);
        board.updateClubBoard(request);
        return clubBoardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long boardId,Long userId){
        ClubBoard board = clubBoardRepository.findById(boardId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
        userRepository.findById(userId).orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
        if(!userId.equals(board.getUserId())) throw new BaseException(ErrorCode.FORBIDDEN_REQUEST);
    }
}
