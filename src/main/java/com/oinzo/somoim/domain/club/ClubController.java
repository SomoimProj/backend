package com.oinzo.somoim.domain.club;

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
    public List<Club> readClubByName(@RequestBody String name) {
        return clubService.readClubByName(name);
    }

    @GetMapping("/favorite")
    public List<Club> readClubByFavorite(@RequestBody Club request){
        return clubService.readClubByFavorite(request.getFavorite(),request.getArea());
    }

    @GetMapping("/{clubId}")
    public Club readClubById(@PathVariable("clubId") Long clubId, HttpServletResponse response,
                                        @CookieValue(value="count", required=false) Cookie countCookie){
        Optional<Club> club = clubService.readClubById(clubId);
        Integer newCnt = clubService.updateCookie(response,countCookie,clubId,club.get().getCnt());
        clubService.updateCnt(clubId,newCnt);
        return club.get();
    }
}
