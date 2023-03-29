package com.oinzo.somoim.domain.album.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.AlbumCreateRequest;
import com.oinzo.somoim.controller.dto.AlbumResponse;
import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import com.oinzo.somoim.domain.album.repository.ClubAlbumRepository;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ClubAlbumService {
    private final ClubAlbumRepository clubAlbumRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubUserRepository clubUserRepository;

    public AlbumResponse addAlbum(AlbumCreateRequest request, Long userId, Long clubId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));
        if(!clubRepository.existsById(clubId))
                throw new BaseException(ErrorCode.WRONG_CLUB);
        if(!clubUserRepository.existsByUser_IdAndClub_Id(userId,clubId)) throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        return AlbumResponse.from(clubAlbumRepository.save(ClubAlbum.from(request,user,clubId)));
    }

    public List<AlbumResponse> readAllAlbum(Long clubId,Pageable pageable){
        if (!clubRepository.existsById(clubId)) {
            throw new BaseException(ErrorCode.WRONG_CLUB);
        }
        List<ClubAlbum> albumList = clubAlbumRepository.findAllByClubId(clubId,pageable).getContent();
        return AlbumResponse.listToAlbumResponse(albumList);
    }

    public AlbumResponse readOneAlbum(Long albumId,Long userId){
        if (!userRepository.existsById(userId)) {
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        }
        ClubAlbum album = clubAlbumRepository.findById(albumId)
                .orElseThrow(()-> new BaseException(ErrorCode.WRONG_ALBUM));
        if(!clubUserRepository.existsByUser_IdAndClub_Id(userId,album.getClubId())) throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        return AlbumResponse.from(album);
    }

    @Transactional
    public void deleteAlbum(Long albumId, Long userId){
        if (!userRepository.existsById(userId)) {
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        }
        ClubAlbum album = clubAlbumRepository.findById(albumId)
                .orElseThrow(()-> new BaseException(ErrorCode.WRONG_ALBUM));
        if(!Objects.equals(album.getUser().getId(),userId)) throw new BaseException(ErrorCode.FORBIDDEN_REQUEST,"작성자가 아닙니다.");
        clubAlbumRepository.delete(album);
    }
}