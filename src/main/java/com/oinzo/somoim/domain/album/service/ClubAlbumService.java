package com.oinzo.somoim.domain.album.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.album.dto.AlbumCreateRequest;
import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import com.oinzo.somoim.domain.album.repository.ClubAlbumRepository;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
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

    // Todo: 클럽 멤버가 아닐 시 앨범 업로드 불가
    @Transactional
    public ClubAlbum addAlbum(AlbumCreateRequest request, Long userId, Long clubId){
        userRepository.findById(userId).orElseThrow(()->new BaseException(ErrorCode.USER_NOT_FOUND));
        clubRepository.findById(clubId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_CLUB));
        return clubAlbumRepository.save(ClubAlbum.from(request,userId,clubId));
    }

    public List<ClubAlbum> readAllAlbum(Long clubId){
        clubRepository.findById(clubId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_CLUB));
        Pageable pageable = Pageable.ofSize(clubRepository.findAll().size());
        return clubAlbumRepository.findAllByClubIdIs(clubId,pageable).getContent();
    }

    public List<ClubAlbum> readAllAlbumPaging(Long clubId,Pageable pageable){
        clubRepository.findById(clubId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_CLUB));
        return clubAlbumRepository.findAllByClubIdIs(clubId,pageable).getContent();
    }

    // Todo: 클럽 멤버가 아닐 시 상세 조회 불가
    public ClubAlbum readOneAlbum(Long albumId){
        return clubAlbumRepository.findById(albumId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_ALBUM));
    }

    @Transactional
    public void deleteAlbum(Long albumId, Long userId){
        ClubAlbum clubAlbum = clubAlbumRepository.findById(albumId).orElseThrow(()-> new BaseException(ErrorCode.WRONG_ALBUM));
        User user = userRepository.findById(userId).orElseThrow(()->new BaseException(ErrorCode.USER_NOT_FOUND));
        if(!Objects.equals(clubAlbum.getUserId(), user.getId())) throw new BaseException(ErrorCode.FORBIDDEN_REQUEST);
        clubAlbumRepository.delete(clubAlbum);
    }
}
