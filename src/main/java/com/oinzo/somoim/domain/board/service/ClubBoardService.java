package com.oinzo.somoim.domain.board.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Category;
import com.oinzo.somoim.controller.dto.BoardCreateRequest;
import com.oinzo.somoim.controller.dto.BoardResponse;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.boardcomment.repository.BoardCommentRepository;
import com.oinzo.somoim.domain.boardlike.repository.BoardLikeRepository;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClubBoardService {
    private final ClubBoardRepository clubBoardRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final ClubUserRepository clubUserRepository;

    private final BoardLikeRepository likeRepository;

    private final BoardCommentRepository commentRepository;

    @Transactional
    public BoardResponse addBoard(BoardCreateRequest request, Long clubId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_CLUB));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId, clubId))
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        return BoardResponse.from(clubBoardRepository.save(ClubBoard.from(request, club, user)), 0, 0);
    }

    public Page<BoardResponse> clubBoardList(Long clubId, Pageable pageable) {
        if (!clubRepository.existsById(clubId)) {
            throw new BaseException(ErrorCode.WRONG_CLUB);
        }
        Page<ClubBoard> boards = clubBoardRepository.findAllByClubIdIs(clubId, pageable);
        return new PageImpl<>(boards.stream()
                .map(board -> BoardResponse.from(board,
                        likeRepository.countAllByBoard_Id(board.getId()),
                        commentRepository.countAllByBoard_Id(board.getId())))
                .collect(Collectors.toList()));
    }

    public List<BoardResponse> clubBoardListAll(Long clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new BaseException(ErrorCode.WRONG_CLUB);
        }
        List<ClubBoard> boards = clubBoardRepository.findAllByClubIdIs(clubId);
        return boards.stream()
                .map(board -> BoardResponse.from(board,
                        likeRepository.countAllByBoard_Id(board.getId()),
                        commentRepository.countAllByBoard_Id(board.getId())))
                .collect(Collectors.toList());
    }

    public Page<BoardResponse> clubBoardListByCategory(Long clubId, String category, Pageable pageable) {
        Category newCategory = Category.valueOfOrHandleException(category);
        if (!clubRepository.existsById(clubId)) {
            throw new BaseException(ErrorCode.WRONG_CLUB);
        }
        Page<ClubBoard> boards = clubBoardRepository.findAllByClubIdIsAndCategory(clubId, newCategory, pageable);
        return new PageImpl<>(boards.stream()
                .map(board -> BoardResponse.from(board,
                        likeRepository.countAllByBoard_Id(board.getId()),
                        commentRepository.countAllByBoard_Id(board.getId())))
                .collect(Collectors.toList()));
    }

    public List<BoardResponse> allClubBoardListByCategory(Long clubId, String category) {
        Category newCategory = Category.valueOfOrHandleException(category);
        if (!clubRepository.existsById(clubId)) {
            throw new BaseException(ErrorCode.WRONG_CLUB);
        }
        List<ClubBoard> boards = clubBoardRepository.findAllByClubIdIsAndCategory(clubId, newCategory);
        return boards.stream()
                .map(board -> BoardResponse.from(board,
                        likeRepository.countAllByBoard_Id(board.getId()),
                        commentRepository.countAllByBoard_Id(board.getId())))
                .collect(Collectors.toList());
    }

    public BoardResponse readBoard(Long boardId, Long userId) {
        ClubBoard board = clubBoardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_BOARD));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId, board.getClub().getId()))
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        return BoardResponse.from(board,
                likeRepository.countAllByBoard_Id(boardId),
                commentRepository.countAllByBoard_Id(boardId));
    }

    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardCreateRequest request, Long userId) {
        ClubBoard board = clubBoardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_BOARD));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId, board.getClub().getId()))
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        if (!userId.equals(board.getUser().getId())) {
            throw new BaseException(ErrorCode.FORBIDDEN_REQUEST, "게시판 작성자가 아닙니다.");
        }
        board.updateClubBoard(request);
        return BoardResponse.from(clubBoardRepository.save(board),
                likeRepository.countAllByBoard_Id(boardId),
                commentRepository.countAllByBoard_Id(boardId));
    }

    @Transactional
    public void deleteBoard(Long boardId, Long userId) {
        ClubBoard board = clubBoardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_BOARD));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId, board.getClub().getId()))
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        if (!userId.equals(board.getUser().getId())) {
            throw new BaseException(ErrorCode.FORBIDDEN_REQUEST, "게시판 작성자가 아닙니다.");
        }
        clubBoardRepository.delete(board);
    }
}
