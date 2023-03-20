package com.oinzo.somoim.domain.club;

import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.club.dto.ClubCreateDto;
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

    @PostMapping()
    public Club addClub(@RequestBody ClubCreateDto request){
        return clubService.addClub(request);
    }

    @GetMapping("/search")
    public List<Club> readClubListByName(@RequestBody ClubRequestDto request) {
        return clubService.readClubListByName(request);
    }

    @GetMapping("/favorite")
    public List<Club> readClubByListFavorite(@RequestBody ClubRequestDto request){
        return clubService.readClubListByFavorite(request);
    }

    @GetMapping("/{clubId}")
    public Club readClubById(@PathVariable("clubId") Long clubId, HttpServletResponse response,
                                       @CookieValue(value="count", required=false) Cookie countCookie){
        return clubService.readClubById(clubId,response,countCookie);
    }

    @GetMapping("/random")
    public List<Club> readClubListByArea(@RequestBody ClubRequestDto request){
        return clubService.readClubByArea(request);
    }


}
