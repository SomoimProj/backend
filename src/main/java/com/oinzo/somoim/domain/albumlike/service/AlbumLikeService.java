package com.oinzo.somoim.domain.albumlike.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.AlbumLikeResponse;
import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import com.oinzo.somoim.domain.album.repository.ClubAlbumRepository;
import com.oinzo.somoim.domain.albumlike.entity.AlbumLike;
import com.oinzo.somoim.domain.albumlike.repository.AlbumLikeRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AlbumLikeService {

    private final ClubAlbumRepository clubAlbumRepository;
    private final AlbumLikeRepository likeRepository;
    private final ClubUserRepository clubUserRepository;

    public AlbumLikeResponse addLike(Long userId, Long albumId) {
        // UserId -> AuthenticationPrincipal을 통해 식별된 userId
        ClubAlbum album = clubAlbumRepository.findById(albumId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_ALBUM));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId, album.getClubId())) {
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        }
        if (likeRepository.existsByAlbum_IdAndUser(albumId, userId)) {
            throw new BaseException(ErrorCode.ALREADY_LIKED);
        }
        AlbumLike albumLike = AlbumLike.builder()
                .album(album)
                .user(userId)
                .build();
        return AlbumLikeResponse.from(likeRepository.save(albumLike));
    }

    public void deleteLike(Long userId, Long albumId) {
        // UserId -> AuthenticationPrincipal을 통해 식별된 userId
        if (!clubAlbumRepository.existsById(albumId)) {
            throw new BaseException(ErrorCode.WRONG_ALBUM);
        }
        AlbumLike like = likeRepository.findByAlbum_IdAndUser(albumId, userId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_LIKE));
        likeRepository.delete(like);
    }

    public List<AlbumLikeResponse> readAllLikeList(Long albumId) {
        if (!clubAlbumRepository.existsById(albumId)) {
            throw new BaseException(ErrorCode.WRONG_ALBUM);
        }
        List<AlbumLike> likes = likeRepository.findAllByAlbum_Id(albumId);
        return AlbumLikeResponse.albumLikeToList(likes);
    }
}
