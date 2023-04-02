package com.oinzo.somoim.domain.boardlike.service;

import com.oinzo.somoim.common.type.Category;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.controller.dto.BoardLikeResponse;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.boardlike.entity.BoardLike;
import com.oinzo.somoim.domain.boardlike.repository.BoardLikeRepository;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardLikeServiceTest {

    @InjectMocks
    private BoardLikeService boardLikeService;

    @Mock
    BoardLikeRepository boardLikeRepository;

    @Mock
    ClubBoardRepository clubBoardRepository;

    @Mock
    ClubUserRepository clubUserRepository;

    @Test
    void addLike() {
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
        given(clubBoardRepository.findById(anyLong())).willReturn(Optional.of(mockBoard));
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(), anyLong())).willReturn(true);
        given(boardLikeRepository.existsByBoard_IdAndUserId(anyLong(), anyLong())).willReturn(false);
        when(boardLikeRepository.save(any(BoardLike.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        ArgumentCaptor<BoardLike> captor = ArgumentCaptor.forClass(BoardLike.class);

        //when
        boardLikeService.addLike(1L, 1L);

        //then
        verify(boardLikeRepository, times(1)).save(captor.capture());
        assertEquals(1L, captor.getValue().getUserId());
        assertEquals(1L, captor.getValue().getBoard().getId());
    }

    @Test
    void readAllLike() {
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
        BoardLike mockLike1 = BoardLike.builder()
                .board(mockBoard)
                .userId(1L)
                .build();
        BoardLike mockLike2 = BoardLike.builder()
                .board(mockBoard)
                .userId(2L)
                .build();
        List<BoardLike> likes = List.of(
                mockLike1,
                mockLike2
        );
        given(clubBoardRepository.existsById(anyLong()))
                .willReturn(true);
        given(boardLikeRepository.findAllByBoard_Id(anyLong()))
                .willReturn(likes);

        // when
        List<BoardLikeResponse> responses = boardLikeService.readAllLike(1L);

        // then
        assertEquals(2,responses.size());
    }
}