package com.oinzo.somoim.domain.album.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.controller.dto.AlbumCreateRequest;
import com.oinzo.somoim.controller.dto.AlbumResponse;
import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import com.oinzo.somoim.domain.album.repository.ClubAlbumRepository;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
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
class ClubAlbumServiceTest {
    @InjectMocks
    private ClubAlbumService clubAlbumService;
    @Mock
    private ClubAlbumRepository clubAlbumRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClubRepository clubRepository;
    @Mock
    private ClubUserRepository clubUserRepository;

    @Test
    @DisplayName("앨범 등록")
    void addAlbum() {
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
        given(clubRepository.existsById(anyLong()))
                .willReturn(true);
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(mockUser));
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(),anyLong()))
                .willReturn(true);
        when(clubAlbumRepository.save(any(ClubAlbum.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        ArgumentCaptor<ClubAlbum> captor = ArgumentCaptor.forClass(ClubAlbum.class);

        // when
        AlbumCreateRequest request = AlbumCreateRequest.builder()
                .imageUrl("URL").build();
        clubAlbumService.addAlbum(request,mockUser.getId(),1L);

        // then
        verify(clubAlbumRepository,times(1)).save(captor.capture());
        assertEquals("URL",captor.getValue().getImageUrl());


    }

    @Test
    @DisplayName("조회: 클럽 멤버가 아닌 경우")
    void readOneAlbum_notClubMember() {
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
        ClubAlbum album = ClubAlbum.builder()
                .id(1L)
                .clubId(1L)
                .user(mockUser)
                .imageUrl("URL")
                .build();
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(),anyLong()))
                .willReturn(false);
        given(userRepository.existsById(anyLong()))
                .willReturn(true);
        given(clubAlbumRepository.findById(anyLong()))
                .willReturn(Optional.of(album));

        // when
        BaseException exception = assertThrows(BaseException.class,
                () -> clubAlbumService.readOneAlbum(1L,1L));

        // then
        assertEquals(ErrorCode.NOT_CLUB_MEMBER,exception.getErrorCode());
    }

    @Test
    @DisplayName("조회 : 성공")
    void readOneAlbum(){
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
        ClubAlbum album = ClubAlbum.builder()
                .id(1L)
                .clubId(1L)
                .user(mockUser)
                .imageUrl("URL")
                .build();
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(),anyLong()))
                .willReturn(true);
        given(userRepository.existsById(anyLong()))
                .willReturn(true);
        given(clubAlbumRepository.findById(anyLong()))
                .willReturn(Optional.of(album));

        // when
        AlbumResponse response = clubAlbumService.readOneAlbum(1L,1L);

        // then
        assertEquals("URL",response.getImageUrl());
    }

}