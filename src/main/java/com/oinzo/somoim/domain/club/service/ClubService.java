package com.oinzo.somoim.domain.club.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.dto.ClubCreateRequest;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    public Club addClub(ClubCreateRequest request) {
        return clubRepository.save(Club.from(request));
    }

    public List<Club> readClubListByName(String name){
        if (name.isBlank()) {
            throw new BaseException(ErrorCode.NO_SEARCH_NAME);
        }
        return clubRepository.findAllByNameContaining(name);
    }

    public List<Club> readClubListByFavorite(Long userId, String favorite){
        Favorite newFavorite = Favorite.valueOfOrHandleException(favorite);

        String area = getAreaBy(userId);
        if (area.isBlank()) {
            throw new BaseException(ErrorCode.NOT_SET_AREA);
        }

        return clubRepository.findAllByFavoriteAndAreaContaining(newFavorite, area);
    }

    public Club readClubById(Long clubId,HttpServletResponse response, Cookie countCookie){
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId));

        Integer newCnt = updateCookie(response, countCookie, clubId, club.getViewCnt());
        updateCnt(club, newCnt);
        return club;
    }

    public Page<Club> readClubListByArea(Long userId, Pageable pageable){
        String area = getAreaBy(userId);
        if (area.isBlank()) {
            throw new BaseException(ErrorCode.NOT_SET_AREA);
        }
        return clubRepository.findAllByAreaLikeOrderByViewCntDesc(area,pageable);
    }

    public Page<Club> readClubListByCreateAt(Long userId, Pageable pageable){
        String area = getAreaBy(userId);
        if (area.isBlank()) {
            throw new BaseException(ErrorCode.NOT_SET_AREA);
        }
        return clubRepository.findAllByAreaLikeOrderByCreatedAtDesc(area, pageable);
    }

    private String getAreaBy(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "userId=" + userId));
        return user.getArea();
    }

    public Integer updateCookie(HttpServletResponse response, Cookie countCookie, Long clubId, Integer clubCnt){
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
                if (Objects.equals(counts[0], clubId.toString()))
                {
                    newValue = cookieCount;
                    clubCnt -= 1;
                }
            }
            countCookie.setValue(newValue);
            countCookie.setMaxAge(countCookie.getMaxAge());
            countCookie.setPath("/");
            response.addCookie(countCookie);
        } else {
            Cookie newCookie = new Cookie("count",clubId.toString());
            newCookie.setMaxAge(60*60*24);
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }
        return clubCnt;
    }

    public void updateCnt(Club club, Integer newCnt){
        clubRepository.save(club.setViewCnt(newCnt));
    }

}
