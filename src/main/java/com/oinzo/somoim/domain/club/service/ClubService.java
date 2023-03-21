package com.oinzo.somoim.domain.club.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.dto.ClubCreateDto;
import com.oinzo.somoim.domain.club.dto.ClubRequestDto;
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

    public Club addClub(ClubCreateDto club){
        String favorite = club.getFavorite();
        try{
            Favorite.valueOf(favorite);
            return clubRepository.save(Club.from(club));
        } catch (IllegalArgumentException e)  {
            throw new BaseException(ErrorCode.WRONG_FAVORITE,ErrorCode.WRONG_FAVORITE.getMessage());
        }
    }

    public List<Club> readClubListByName(ClubRequestDto request){
        try{
            String name = request.getName();
            List<Club> result = clubRepository.findAllByNameContaining(name);
            if(name.length()<1) throw new BaseException(ErrorCode.NO_SEARCH_NAME,ErrorCode.NO_SEARCH_NAME.getMessage());
            else if (result.isEmpty()) throw new BaseException(ErrorCode.NO_DATA_FOUND,ErrorCode.NO_DATA_FOUND.getMessage());
            else return clubRepository.findAllByNameContaining(name);
        }catch (IllegalArgumentException e) {
            throw new BaseException(ErrorCode.NO_SEARCH_NAME,ErrorCode.NO_SEARCH_NAME.getMessage());
        }
    }

    public List<Club> readClubListByFavorite(ClubRequestDto request){
        String favorite = request.getFavorite();
        String area = request.getArea();
        List<Club> result = clubRepository.findAllByFavoriteContainingAndAreaContaining(favorite,area);
        if (favorite.length()<1)
            throw new BaseException(ErrorCode.NO_SEARCH_NAME,ErrorCode.NO_SEARCH_NAME.getMessage());
        try{
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
        Optional<Club> club = clubRepository.findById(clubId);
        if(club.isPresent())
        {
            Integer newCnt = updateCookie(response,countCookie,clubId,club.get().getViewCnt());
            updateCnt(club.get(),newCnt);
            return club.get();
        } else throw new BaseException(ErrorCode.WRONG_CLUB,ErrorCode.WRONG_CLUB.getMessage());
    }

    public List<Club> readClubByArea(ClubRequestDto request){
        if(request.getArea().isEmpty())
            throw  new BaseException(ErrorCode.NO_DATA_FOUND,ErrorCode.NO_DATA_FOUND.getMessage());
        return clubRepository.findAllByAreaLikeOrderByViewCntDesc(request.getArea());
    }

    public List<Club> readClubByCreateAt(String request){
        if(request.length()<1)
            throw  new BaseException(ErrorCode.NO_DATA_FOUND,ErrorCode.NO_DATA_FOUND.getMessage());
        return clubRepository.findAllByAreaLikeOrderByCreatedAtDesc(request);
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
            } else {
                if(Objects.equals(counts[0], clubId.toString()))
                {
                    newValue = cookieCount;
                    clubCnt -=1;
                }
            }
            countCookie.setValue(newValue);
            countCookie.setMaxAge(countCookie.getMaxAge());
            countCookie.setPath("/");
            response.addCookie(countCookie);
            return clubCnt;
        } else {
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
