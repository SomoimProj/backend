package com.oinzo.somoim.domain.club;

import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.club.dto.ClubRequestDto;
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
    public List<Club> readClubByName(@RequestBody ClubRequestDto request) {
        return clubService.readClubByName(request);
    }

    @GetMapping("/favorite")
    public List<Club> readClubByFavorite(@RequestBody ClubRequestDto request){
        return clubService.readClubByFavorite(request);
    }

    @GetMapping("/{clubId}")
    public Optional<Club> readClubById(@PathVariable("clubId") Long clubId, HttpServletResponse response,
                                       @CookieValue(value="count", required=false) Cookie countCookie){
        return clubService.readClubById(clubId,response,countCookie);
    }

    @GetMapping("/random")
    public List<Club> readClubByArea(@RequestBody ClubRequestDto request){
        return clubService.readClubByArea(request);
    }


}
