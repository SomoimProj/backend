package com.oinzo.somoim.domain.club;

import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.service.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/clubs")
public class ClubController {
    private final ClubService clubService;

    @ResponseBody
    @PostMapping()
    public Club addClub(@RequestBody Club request){
        return clubService.addClub(request);
    }

    @GetMapping("/search")
    public Object readClubByName(@RequestBody String name) {
        if(clubService.readClubByName(name).isEmpty())
            return ErrorCode.NO_DATA_FOUND;
        else {return clubService.readClubByName(name);}
    }

    @GetMapping("/favorite")
    public List<Club> readClubByFavorite(@RequestBody Club request){
        return clubService.readClubByFavorite(request.getFavorite(),request.getArea());
    }

    @GetMapping("/{clubId}")
    public Object readClubById(@PathVariable("clubId") Long clubId, HttpServletResponse response,
                               @CookieValue(value="count", required=false) Cookie countCookie){
        Optional<Club> club = clubService.readClubById(clubId);
        if(club.isPresent())
        {
            Integer newCnt = clubService.updateCookie(response,countCookie,clubId,club.get().getCnt());
            clubService.updateCnt(clubId,newCnt);
            return club.get();
        }
        else return ErrorCode.WRONG_CLUB;
    }

    @GetMapping("/random")
    public Object readClubByArea(@RequestBody Club request){
        List<Club> result = clubService.readClubByArea(request.getArea());
        if(result.isEmpty())
            return ErrorCode.NO_DATA_FOUND;
        else return clubService.readClubByArea(request.getArea());
    }


}
