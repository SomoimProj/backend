package com.oinzo.somoim.domain.club.service;

import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.dto.ClubCreateDto;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    public Object addClub(Club club){
        String favorite = club.getFavorite();
        try{
            Favorite.valueOf(favorite);
            return clubRepository.save(Club.from(club));
        } catch (IllegalArgumentException e)  {
            return ErrorCode.WRONG_FAVORITE;
        }
    }

    public Object readClubByName(Club request){
        try{
            String name = request.getName();
            List<Club> result = clubRepository.findAllByNameContaining(name);
            if(name.length()<1) { return ErrorCode.NO_SEARCH_NAME; }
            else if (result.isEmpty()) { return ErrorCode.NO_DATA_FOUND; }
            else{ return clubRepository.findAllByNameContaining(name); }
        }catch (IllegalArgumentException e) {
            return ErrorCode.NO_SEARCH_NAME;
        }
    }

    public Object readClubByFavorite(Club request){
        String favorite = request.getFavorite();
        String area = request.getArea();
        List<Club> result = clubRepository.findAllByFavoriteContainingAndAreaContaining(favorite,area);
        if(favorite.length()<1) { return ErrorCode.NO_SEARCH_NAME; }
        try{
            Favorite.valueOf(favorite);
            if (result.isEmpty()) { return ErrorCode.NO_DATA_FOUND; }
            else { return result; }
        } catch (IllegalArgumentException e)  {
            return ErrorCode.WRONG_FAVORITE;
        }
    }

    public Object readClubById(Long clubId,HttpServletResponse response, Cookie countCookie ){
        Optional<Club> club = clubRepository.findById(clubId);
        if(club.isPresent())
        {
            Integer newCnt = updateCookie(response,countCookie,clubId,club.get().getCnt());
            updateCnt(club.get(),newCnt);
            return club.get();
        }
        else return ErrorCode.WRONG_CLUB;
    }

    public Object readClubByArea(Club request){
        if(request.getArea().isEmpty())
            return ErrorCode.NO_DATA_FOUND;
        return clubRepository.findAllByAreaLikeOrderByCntDesc(request.getArea());
    }

    public Integer updateCookie(HttpServletResponse response, Cookie countCookie, Long clubId, Integer clubCnt){
        clubCnt += 1;
        if(countCookie!=null){
            String cookieCount = countCookie.getValue();
            String newValue = cookieCount+"_"+clubId.toString();
            String[] counts = cookieCount.split("_");

            if(counts.length!=1)
            {
                for (String count : counts) {
                    if (Objects.equals(count, clubId.toString())) {
                        newValue = cookieCount;
                        clubCnt -= 1;
                    }
                }
            }else {
                if(Objects.equals(counts[0], clubId.toString()))
                {
                    newValue = cookieCount;
                    clubCnt -=1;
                }
            }
            countCookie.setValue(newValue);
            countCookie.setMaxAge(60*60*24);
            countCookie.setPath("/");
            response.addCookie(countCookie);
            return clubCnt;
        }
        else {
            Cookie newCookie = new Cookie("count",clubId.toString());
            newCookie.setMaxAge(60*60*24);
            newCookie.setPath("/");
            response.addCookie(newCookie);
            return clubCnt;
        }
    }

    public void updateCnt(Club club, Integer newCnt){
        clubRepository.save(club.setCnt(newCnt));
    }

}
