package com.oinzo.somoim.domain.boardcomment.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.CommentRequest;
import com.oinzo.somoim.controller.dto.CommentResponse;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.boardcomment.entity.BoardComment;
import com.oinzo.somoim.domain.boardcomment.repository.BoardCommentRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BoardCommentService {
    private final ClubBoardRepository clubBoardRepository;
    private final UserRepository userRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final ClubUserRepository clubUserRepository;

    public CommentResponse addComment(CommentRequest request, Long boardId, Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
        ClubBoard board = clubBoardRepository.findById(boardId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId,board.getClub().getId())) throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        return CommentResponse.from(boardCommentRepository.save(BoardComment.from(request,board,user)),user);
    }

    public CommentResponse readOneComment(Long commentId,Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
        BoardComment comment = boardCommentRepository.findById(commentId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_COMMENT));
        ClubBoard clubBoard = clubBoardRepository.findById(comment.getBoard().getId())
                .orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD,"게시글 조회에 실패하였습니다."));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId,clubBoard.getClub().getId())) {
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        }
        return CommentResponse.from(boardCommentRepository.findById(commentId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_COMMENT)),user);
    }

    public List<CommentResponse> readAllComment(Long boardId, Long userId){
        if(!userRepository.existsById(userId)) throw new BaseException(ErrorCode.USER_NOT_FOUND);
        ClubBoard board = clubBoardRepository.findById(boardId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_BOARD));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId,board.getClub().getId())) {
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        }
        List<BoardComment> resultList = boardCommentRepository.findAllByBoardId(board.getId());
        List<CommentResponse> result = new ArrayList<>();

        for (BoardComment comment : resultList) {
            User userInfo = userRepository.findById(comment.getUser().getId())
                    .orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND,"댓글의 유저정보 조회에 실패하였습니다."));
            result.add(CommentResponse.from(comment, userInfo));
        }
        return result;
    }

    @Transactional
    public CommentResponse updateComment(CommentRequest request, Long commentId, Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
        BoardComment comment = boardCommentRepository.findById(commentId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_COMMENT));
        if(!clubUserRepository.existsByUser_IdAndClub_Id(userId,comment.getBoard().getClub().getId())) throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        if(!Objects.equals(userId, comment.getUser().getId())) throw new BaseException(ErrorCode.FORBIDDEN_REQUEST,"작성자가 아닙니다.");
        comment.updateComment(request);
        return CommentResponse.from(boardCommentRepository.save(comment),user);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId){
        if (!userRepository.existsById(userId)) {
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        }
        BoardComment comment = boardCommentRepository.findById(commentId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_COMMENT));
        if(!clubUserRepository.existsByUser_IdAndClub_Id(userId,comment.getBoard().getClub().getId())) throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        if(!Objects.equals(userId, comment.getUser().getId())) throw new BaseException(ErrorCode.FORBIDDEN_REQUEST,"작성자가 아닙니다.");
        boardCommentRepository.delete(comment);
    }
}