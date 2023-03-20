package com.oinzo.somoim.domain.club.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.dto.ClubCreateRequest;
import com.oinzo.somoim.controller.dto.ClubRequestDto;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    public Club addClub(ClubCreateRequest request) {
        return clubRepository.save(Club.from(request));
    }

    public List<Club> readClubListByName(ClubRequestDto request){
        try {
            String name = request.getName();
            List<Club> result = clubRepository.findAllByNameContaining(name);
            if (name.length() < 1) {
                throw new BaseException(ErrorCode.NO_SEARCH_NAME);
            } else if (result.isEmpty()) {
                throw new BaseException(ErrorCode.NO_DATA_FOUND);
            }
            return clubRepository.findAllByNameContaining(name);
        } catch (IllegalArgumentException e) {
            throw new BaseException(ErrorCode.NO_SEARCH_NAME);
        }
    }

    public List<Club> readClubListByFavorite(ClubRequestDto request){
        String favorite = request.getFavorite();
        String area = request.getArea();
        List<Club> result = clubRepository.findAllByFavoriteContainingAndAreaContaining(favorite,area);
        if (favorite.length() < 1) {
            throw new BaseException(ErrorCode.NO_SEARCH_NAME, ErrorCode.NO_SEARCH_NAME.getMessage());
        }
        try {
            Favorite.valueOf(favorite);
            if (result.isEmpty()) {
                throw new BaseException(ErrorCode.NO_DATA_FOUND,ErrorCode.NO_DATA_FOUND.getMessage());
            } else {
                return result;
            }
        } catch (RuntimeException e)  {
            throw new BaseException(ErrorCode.WRONG_FAVORITE,ErrorCode.WRONG_FAVORITE.getMessage());
        }
    }

    public Club readClubById(Long clubId,HttpServletResponse response, Cookie countCookie ){
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId));

        Integer newCnt = updateCookie(response, countCookie, clubId, club.getViewCnt());
        updateCnt(club, newCnt);
        return club;
    }

    public List<Club> readClubListByArea(ClubRequestDto request){
        if (request.getArea().isEmpty()) {
            throw new BaseException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getMessage());
        }
        return clubRepository.findAllByAreaLikeOrderByViewCntDesc(request.getArea());
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
        clubRepository.save(club.setCnt(newCnt));
    }

}
