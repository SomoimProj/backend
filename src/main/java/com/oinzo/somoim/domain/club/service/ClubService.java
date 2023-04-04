package com.oinzo.somoim.domain.club.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.controller.dto.ClubCreateRequest;
import com.oinzo.somoim.controller.dto.ClubDetailResponse;
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clublike.service.ClubLikeService;
import com.oinzo.somoim.domain.clubuser.entity.ClubUser;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.clubuser.service.ClubUserService;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ClubService {

    private final ClubUserService clubUserService;
    private final ClubLikeService clubLikeService;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubUserRepository clubUserRepository;

    public ClubDetailResponse addClub(Long userId, ClubCreateRequest request) {
        Club club = Club.from(request);
        Club savedClub = clubRepository.save(club);

        // 클럽 매니저로 등록
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "userId=" + userId));
        ClubUser clubUser = ClubUser.createClubUserManager(user, club);

        clubUserRepository.save(clubUser);

        Long memberCnt = clubUserService.readMembersCount(club.getId());
        Long likeCnt = clubLikeService.readLikesCount(club.getId());
        return ClubDetailResponse.fromClubAndManagerIdAndMemberCntAndLikeCnt(
            savedClub, user.getId(), memberCnt, likeCnt);
    }

    public List<ClubResponse> readClubListByName(String name) {
        if (name.isBlank()) {
            throw new BaseException(ErrorCode.NO_SEARCH_NAME);
        }
        List<Club> clubList = clubRepository.findAllByNameContaining(name);
        return clubList.stream()
            .map(club -> {
                Long memberCnt = clubUserService.readMembersCount(club.getId());
                return ClubResponse.fromClubAndMemberCnt(club, memberCnt);
            })
            .collect(Collectors.toList());
    }

    public List<ClubResponse> readClubListByFavorite(Long userId, String favorite) {
        Favorite newFavorite = Favorite.valueOfOrHandleException(favorite);

        String area = getAreaBy(userId);
        if (area.isBlank()) {
            throw new BaseException(ErrorCode.NOT_SET_AREA);
        }

        List<Club> clubList = clubRepository.findAllByFavoriteAndAreaContaining(newFavorite, area);
        return clubList.stream()
            .map(club -> {
                Long memberCnt = clubUserService.readMembersCount(club.getId());
                return ClubResponse.fromClubAndMemberCnt(club, memberCnt);
            })
            .collect(Collectors.toList());
    }

    public ClubDetailResponse readClubById(Long clubId, HttpServletResponse response, Cookie countCookie) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId));

        Integer newCnt = updateCookie(response, countCookie, clubId, club.getViewCnt());
        updateCnt(club, newCnt);

        Long managerId = clubUserService.readClubManagerId(clubId);
        Long memberCnt = clubUserService.readMembersCount(club.getId());
        Long likeCnt = clubLikeService.readLikesCount(club.getId());
        return ClubDetailResponse.fromClubAndManagerIdAndMemberCntAndLikeCnt(
            club, managerId, memberCnt, likeCnt);
    }

    public Page<ClubResponse> readClubListByArea(Long userId, Pageable pageable) {
        String area = getAreaBy(userId);
        if (area.isBlank()) {
            throw new BaseException(ErrorCode.NOT_SET_AREA);
        }
        Page<Club> clubs = clubRepository.findAllByAreaLikeOrderByViewCntDescIdDesc(area, pageable);
        return new PageImpl<>(clubs.stream()
                .map(club -> {
                    Long memberCnt = clubUserService.readMembersCount(club.getId());
                    return ClubResponse.fromClubAndMemberCnt(club, memberCnt);
                })
                .collect(Collectors.toList()));
    }

    public Page<ClubResponse> readClubListByCreateAt(Long userId, Pageable pageable) {
        String area = getAreaBy(userId);
        if (area.isBlank()) {
            throw new BaseException(ErrorCode.NOT_SET_AREA);
        }
        Page<Club> clubs = clubRepository.findAllByAreaLikeOrderByCreatedAtDescIdDesc(area, pageable);
        return new PageImpl<>(clubs.stream()
                .map(club -> {
                    Long memberCnt = clubUserService.readMembersCount(club.getId());
                    return ClubResponse.fromClubAndMemberCnt(club, memberCnt);
                })
                .collect(Collectors.toList()));
    }

    private String getAreaBy(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "userId=" + userId));
        return user.getArea();
    }

    public Integer updateCookie(HttpServletResponse response, Cookie countCookie, Long clubId, Integer clubCnt) {
        clubCnt += 1;
        if (countCookie != null) {
            String cookieCount = countCookie.getValue();
            String newValue = cookieCount + "_" + clubId.toString();
            String[] counts = cookieCount.split("_");

            if (counts.length != 1) {
                for (String count : counts) {
                    if (Objects.equals(count, clubId.toString())) {
                        newValue = cookieCount;
                        clubCnt -= 1;
                    }
                }
            } else {
                if (Objects.equals(counts[0], clubId.toString())) {
                    newValue = cookieCount;
                    clubCnt -= 1;
                }
            }
            countCookie.setValue(newValue);
            countCookie.setMaxAge(countCookie.getMaxAge());
            countCookie.setPath("/");
            response.addCookie(countCookie);
        } else {
            Cookie newCookie = new Cookie("count", clubId.toString());
            newCookie.setMaxAge(60 * 60 * 24);
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }
        return clubCnt;
    }

    public void updateCnt(Club club, Integer newCnt) {
        clubRepository.save(club.setViewCnt(newCnt));
    }

    public ClubDetailResponse updateClub(ClubCreateRequest request, Long clubId, Long userId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_CLUB));
        Long managerId = clubUserService.readClubManagerId(clubId);
        if (!Objects.equals(userId, managerId)) {
            throw new BaseException(ErrorCode.NOT_CLUB_MANAGER);
        }
        club.updateClub(request);
        clubRepository.save(club);

        Long memberCnt = clubUserService.readMembersCount(club.getId());
        Long likeCnt = clubLikeService.readLikesCount(club.getId());
        return ClubDetailResponse.fromClubAndManagerIdAndMemberCntAndLikeCnt(club, managerId, memberCnt, likeCnt);
    }

    public List<ClubResponse> readByNameFavorite(String name, String favorite) {
        Favorite newFavorite = Favorite.valueOfOrHandleException(favorite);
        if (name.isBlank()) {
            throw new BaseException(ErrorCode.NO_SEARCH_NAME);
        }
        List<Club> clubs = clubRepository.findAllByNameContainingAndFavorite(name,newFavorite);
        return clubs.stream()
            .map(club -> {
                Long memberCnt = clubUserService.readMembersCount(club.getId());
                return ClubResponse.fromClubAndMemberCnt(club, memberCnt);
            })
            .collect(Collectors.toList());
    }

}
