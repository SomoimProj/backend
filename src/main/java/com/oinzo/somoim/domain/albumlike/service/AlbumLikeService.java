package com.oinzo.somoim.domain.albumlike.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.AlbumLikeResponse;
import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import com.oinzo.somoim.domain.album.repository.ClubAlbumRepository;
import com.oinzo.somoim.domain.albumlike.entity.AlbumLike;
import com.oinzo.somoim.domain.albumlike.repository.AlbumLikeRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AlbumLikeService {
    private final UserRepository userRepository;
    private final ClubAlbumRepository clubAlbumRepository;
    private final AlbumLikeRepository likeRepository;
    private final ClubUserRepository clubUserRepository;

    public AlbumLikeResponse addLike(Long userId, Long albumId){
        if(!userRepository.existsById(userId))
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        ClubAlbum album = clubAlbumRepository.findById(albumId)
                .orElseThrow(()-> new BaseException(ErrorCode.WRONG_ALBUM));
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId,album.getClubId())) {
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        }
        if(likeRepository.existsByAlbumIdAndUserId(albumId,userId))
            throw new BaseException(ErrorCode.ALREADY_LIKED);
        return AlbumLikeResponse.from(likeRepository.save(AlbumLike.from(userId,album)));
    }

    public void deleteLike(Long userId,Long albumId){
        if(!userRepository.existsById(userId))
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        if(!clubAlbumRepository.existsById(albumId))
            throw new BaseException(ErrorCode.WRONG_ALBUM);
        AlbumLike like = likeRepository.findByAlbumIdAndUserId(albumId,userId)
            .orElseThrow(()-> new BaseException(ErrorCode.WRONG_LIKE));
        likeRepository.delete(like);
    }
}
