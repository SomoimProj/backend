package com.oinzo.somoim.domain.club.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.controller.dto.ClubCreateRequest;
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
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

    public ClubResponse addClub(ClubCreateRequest request) {
        return ClubResponse.from(clubRepository.save(Club.from(request)));
    }

    public List<ClubResponse> readClubListByName(String name){
        try{
            List<Club> result = clubRepository.findAllByNameContaining(name);
            if (name.length() < 1) {
                throw new BaseException(ErrorCode.NO_SEARCH_NAME);
            } else if (result.isEmpty()) {
                throw new BaseException(ErrorCode.NO_DATA_FOUND);
            }
            List<Club> clubList = clubRepository.findAllByNameContaining(name);
            return ClubResponse.listToBoardResponse(clubList);
        } catch (IllegalArgumentException e) {
            throw new BaseException(ErrorCode.NO_SEARCH_NAME);
        }
    }

    public List<ClubResponse> readClubListByFavorite(String favorite,String area){
        Favorite newFavorite = Favorite.valueOfOrHandleException(favorite);
        List<Club> result = clubRepository.findAllByFavoriteAndAreaContaining(newFavorite,area);
        if (favorite.length()<1)
            throw new BaseException(ErrorCode.NO_SEARCH_NAME,ErrorCode.NO_SEARCH_NAME.getMessage());
        try{
            if (result.isEmpty()) {
                throw new BaseException(ErrorCode.NO_DATA_FOUND,ErrorCode.NO_DATA_FOUND.getMessage());
            } else {
                return ClubResponse.listToBoardResponse(result);
            }
        } catch (RuntimeException e)  {
            throw new BaseException(ErrorCode.NO_DATA_FOUND,ErrorCode.NO_DATA_FOUND.getMessage());
        }
    }

    public ClubResponse readClubById(Long clubId,HttpServletResponse response, Cookie countCookie ){
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId));

        Integer newCnt = updateCookie(response, countCookie, clubId, club.getViewCnt());
        updateCnt(club, newCnt);
        return ClubResponse.from(club);
    }

    public List<ClubResponse> readClubByArea(String area,Pageable pageable){
        if(area.length()<1)
            throw  new BaseException(ErrorCode.NO_DATA_FOUND,ErrorCode.NO_DATA_FOUND.getMessage());
        List<Club> clubList = clubRepository.findAllByAreaLikeOrderByViewCntDesc(area,pageable).getContent();
        return ClubResponse.listToBoardResponse(clubList);
    }

    public List<ClubResponse> readAllClubByArea(String area){
        if(area.length()<1)
            throw  new BaseException(ErrorCode.NO_DATA_FOUND,ErrorCode.NO_DATA_FOUND.getMessage());
        List<Club> clubList = clubRepository.findAllByAreaLike(area);
        Pageable pageable = Pageable.ofSize(clubList.size());
        List<Club> result = clubRepository.findAllByAreaLikeOrderByViewCntDesc(area,pageable).getContent();
        return ClubResponse.listToBoardResponse(result);
    }

    public List<ClubResponse> readClubByCreateAt(String request, Pageable pageable){
        if(request.length()<1)
            throw  new BaseException(ErrorCode.NO_DATA_FOUND,ErrorCode.NO_DATA_FOUND.getMessage());
        List<Club> clubList = clubRepository.findAllByAreaLikeOrderByCreatedAtDesc(request,pageable).getContent();
        return ClubResponse.listToBoardResponse(clubList);
    }

    public List<ClubResponse> readAllClubByCreateAt(String request){
        if(request.length()<1)
            throw  new BaseException(ErrorCode.NO_DATA_FOUND,ErrorCode.NO_DATA_FOUND.getMessage());
        List<Club> clubList = clubRepository.findAllByAreaLike(request);
        Pageable pageable = Pageable.ofSize(clubList.size());
        List<Club> result = clubRepository.findAllByAreaLikeOrderByCreatedAtDesc(request,pageable).getContent();
        return ClubResponse.listToBoardResponse(result);
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
