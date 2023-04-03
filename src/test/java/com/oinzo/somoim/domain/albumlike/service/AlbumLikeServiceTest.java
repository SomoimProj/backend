package com.oinzo.somoim.domain.albumlike.service;

import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.controller.dto.AlbumLikeResponse;
import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import com.oinzo.somoim.domain.album.repository.ClubAlbumRepository;
import com.oinzo.somoim.domain.albumlike.entity.AlbumLike;
import com.oinzo.somoim.domain.albumlike.repository.AlbumLikeRepository;
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
class AlbumLikeServiceTest {
    @InjectMocks
    private AlbumLikeService albumLikeService;

    @Mock
    AlbumLikeRepository albumLikeRepository;

    @Mock
    ClubAlbumRepository clubAlbumRepository;

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
        ClubAlbum mockAlbum = ClubAlbum.builder()
                .id(1L)
                .user(mockUser)
                .clubId(1L)
                .imageUrl("URL")
                .build();


        given(clubAlbumRepository.findById(anyLong())).willReturn(Optional.of(mockAlbum));
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(), anyLong())).willReturn(true);
        given(albumLikeRepository.existsByAlbum_IdAndUser(anyLong(), anyLong())).willReturn(false);
        when(albumLikeRepository.save(any(AlbumLike.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        ArgumentCaptor<AlbumLike> captor = ArgumentCaptor.forClass(AlbumLike.class);

        //when
        albumLikeService.addLike(1L, 1L);

        //then
        verify(albumLikeRepository, times(1)).save(captor.capture());
        assertEquals(1L, captor.getValue().getUser());
        assertEquals(1L, captor.getValue().getAlbum().getId());
    }

    @Test
    void readAllLike() {
        //given
        User mockUser = User.builder()
                .id(1L)
                .name("테스트")
                .birth(LocalDate.parse("2023-03-28"))
                .gender(Gender.MALE)
                .area("서울")
                .introduction("테스트")
                .favorites(List.of(Favorite.GAME))
                .build();
        ClubAlbum mockAlbum1 = ClubAlbum.builder()
                .id(1L)
                .user(mockUser)
                .clubId(1L)
                .imageUrl("URL")
                .build();
        AlbumLike mockLike1 = AlbumLike.builder()
                .id(1L)
                .album(mockAlbum1)
                .user(1L)
                .build();
        AlbumLike mockLike2 = AlbumLike.builder()
                .id(2L)
                .album(mockAlbum1)
                .user(2L)
                .build();
        List<AlbumLike> likeList = List.of(
                mockLike1,
                mockLike2
        );
        given(clubAlbumRepository.existsById(anyLong()))
                .willReturn(true);
        given(albumLikeRepository.findAllByAlbum_Id(anyLong()))
                .willReturn(likeList);

        // when
        List<AlbumLikeResponse> likes = albumLikeService.readAllLikeList(1L);

        // then
        assertEquals(2,likes.size());

    }
}