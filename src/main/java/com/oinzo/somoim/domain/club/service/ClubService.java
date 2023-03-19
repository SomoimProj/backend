package com.oinzo.somoim.domain.club.service;

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

    public Club addClub(Club club){
        return clubRepository.save(Club.from(club));
    }

    public List<Club> readClubByName(String name){
        return clubRepository.findAllByNameContaining(name);
    }

    public List<Club> readClubByFavorite(String favorite, String area){
        return clubRepository.findAllByFavoriteContainingAndAreaContaining(favorite,area);
    }

    public Optional<Club> readClubById(Long clubId){
        return clubRepository.findById(clubId);
    }

    public List<Club> readClubByArea(String area){
        return clubRepository.findAllByAreaLikeOrderByCntDesc(area);
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

    public void updateCnt(Long clubId, Integer cnt){
        Optional<Club> club2 = clubRepository.findById(clubId);
        club2.ifPresent(clubRepository::save);
    }

}
