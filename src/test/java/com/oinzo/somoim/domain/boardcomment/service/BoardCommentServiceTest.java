package com.oinzo.somoim.domain.boardcomment.service;

import com.oinzo.somoim.common.type.Category;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.controller.dto.CommentRequest;
import com.oinzo.somoim.controller.dto.CommentResponse;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.boardcomment.entity.BoardComment;
import com.oinzo.somoim.domain.boardcomment.repository.BoardCommentRepository;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BoardCommentServiceTest {

    @InjectMocks
    private BoardCommentService commentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoardCommentRepository commentRepository;
    @Mock
    private ClubBoardRepository boardRepository;
    @Mock
    private ClubUserRepository clubUserRepository;

    @Test
    void addComment() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .name("테스트")
                .birth(LocalDate.parse("2023-03-28"))
                .gender(Gender.MALE)
                .area("서울")
                .introduction("테스트")
                .favorites(List.of(Favorite.GAME))
                .build();
        Club mockClub = Club.builder()
                .id(1L)
                .name("테스트 클럽")
                .description("테스트 클럽입니다.")
                .area("서울")
                .memberLimit(1)
                .memberCnt(0)
                .favorite(Favorite.GAME)
                .build();
        ClubBoard mockBoard = ClubBoard.builder()
                .id(1L)
                .user(mockUser)
                .club(mockClub)
                .category(Category.FREE)
                .title("테스트")
                .content("테스트 게시판")
                .imageUrl("URL")
                .build();
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(mockUser));
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(mockBoard));
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(),anyLong()))
                .willReturn(true);
        when(commentRepository.save(any(BoardComment.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        ArgumentCaptor<BoardComment> captor = ArgumentCaptor.forClass(BoardComment.class);

        // when
        CommentRequest request = CommentRequest.builder()
                .comment("테스트 댓글")
                .build();
        commentService.addComment(request,1L,1L);

        // then
        verify(commentRepository,times(1)).save(captor.capture());
        assertEquals("테스트 댓글",captor.getValue().getComment());
    }

    @Test
    void readOneComment() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .name("테스트")
                .birth(LocalDate.parse("2023-03-28"))
                .gender(Gender.MALE)
                .area("서울")
                .introduction("테스트")
                .favorites(List.of(Favorite.GAME))
                .build();
        Club mockClub = Club.builder()
                .id(1L)
                .name("테스트 클럽")
                .description("테스트 클럽입니다.")
                .area("서울")
                .memberLimit(1)
                .memberCnt(0)
                .favorite(Favorite.GAME)
                .build();
        ClubBoard mockBoard = ClubBoard.builder()
                .id(1L)
                .user(mockUser)
                .club(mockClub)
                .category(Category.FREE)
                .title("테스트")
                .content("테스트 게시판")
                .imageUrl("URL")
                .build();
        BoardComment comment = BoardComment.builder()
                .id(1L)
                .user(mockUser)
                .board(mockBoard)
                .comment("테스트 댓글")
                .build();
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(mockUser));
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(mockBoard));
        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment));
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(),anyLong()))
                .willReturn(true);

        // when
        CommentResponse response = commentService.readOneComment(1L,1L);

        // then
        assertEquals("테스트 댓글",response.getComment());
    }

    @Test
    void updateComment() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .name("테스트")
                .birth(LocalDate.parse("2023-03-28"))
                .gender(Gender.MALE)
                .area("서울")
                .introduction("테스트")
                .favorites(List.of(Favorite.GAME))
                .build();
        Club mockClub = Club.builder()
                .id(1L)
                .name("테스트 클럽")
                .description("테스트 클럽입니다.")
                .area("서울")
                .memberLimit(1)
                .memberCnt(0)
                .favorite(Favorite.GAME)
                .build();
        ClubBoard mockBoard = ClubBoard.builder()
                .id(1L)
                .user(mockUser)
                .club(mockClub)
                .category(Category.FREE)
                .title("테스트")
                .content("테스트 게시판")
                .imageUrl("URL")
                .build();
        BoardComment comment = BoardComment.builder()
                .user(mockUser)
                .board(mockBoard)
                .comment("테스트 댓글").build();
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(mockUser));
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(),anyLong()))
                .willReturn(true);
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(mockBoard));
        when(commentRepository.save(any(BoardComment.class)))
                .then(AdditionalAnswers.returnsFirstArg());
        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment));
        ArgumentCaptor<BoardComment> captor = ArgumentCaptor.forClass(BoardComment.class);

        // when
        CommentRequest request = CommentRequest.builder()
                .comment("테스트 댓글2")
                .build();
        commentService.updateComment(request,1L,1L);

        // then
        verify(commentRepository,times(1)).save(captor.capture());
        assertEquals("테스트 댓글2",captor.getValue().getComment());
    }
}