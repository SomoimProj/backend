package com.oinzo.somoim.domain.board.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Category;
import com.oinzo.somoim.controller.dto.BoardCreateRequest;
import com.oinzo.somoim.controller.dto.BoardResponse;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@AllArgsConstructor
public class ClubBoardService {
    private final ClubBoardRepository clubBoardRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    @Transactional
    public BoardResponse addBoard(BoardCreateRequest request, Long clubId, Long userId){
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
        Club club = clubRepository.findById(clubId)
            .orElseThrow(()->new BaseException(ErrorCode.WRONG_CLUB));
        return BoardResponse.from(clubBoardRepository.save(ClubBoard.from(request, club, user)));
    }

    public List<BoardResponse> clubBoardList(Long clubId, Pageable pageable){
        if (!clubRepository.existsById(clubId)) {
            throw new BaseException(ErrorCode.WRONG_CLUB);
        }
        List<ClubBoard> clubBoardList = clubBoardRepository.findAllByClub_IdOrderByIdDesc(clubId, pageable).getContent();
        return BoardResponse.ListToBoardResponse(clubBoardList);
    }

    public List<BoardResponse> clubBoardListByCategory(Long clubId, String category, Pageable pageable){
        Category newCategory = Category.valueOfOrHandleException(category);
        if (!clubRepository.existsById(clubId)) {
            throw new BaseException(ErrorCode.WRONG_CLUB);
        }
        List<ClubBoard> clubBoardList = clubBoardRepository.findAllByClub_IdAndCategoryOrderByIdDesc(clubId, newCategory, pageable).getContent();
        return BoardResponse.ListToBoardResponse(clubBoardList);
    }

    public BoardResponse readBoard(Long boardId){
        ClubBoard board = clubBoardRepository.findById(boardId)
            .orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
        return BoardResponse.from(board);
    }

    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardCreateRequest request, Long userId){
        ClubBoard board = clubBoardRepository.findById(boardId)
            .orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
        if(!userId.equals(board.getUser().getId())) {
            throw new BaseException(ErrorCode.FORBIDDEN_REQUEST, "게시판 작성자가 아닙니다.");
        }
        board.updateClubBoard(request);
        return BoardResponse.from(clubBoardRepository.save(board));
    }

    @Transactional
    public void deleteBoard(Long boardId, Long userId){
        ClubBoard board = clubBoardRepository.findById(boardId)
            .orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
        if(!userId.equals(board.getUser().getId())) {
            throw new BaseException(ErrorCode.FORBIDDEN_REQUEST, "게시판 작성자가 아닙니다.");
        }
        clubBoardRepository.delete(board);
    }
}
