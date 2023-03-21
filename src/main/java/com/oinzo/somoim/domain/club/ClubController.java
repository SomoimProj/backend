package com.oinzo.somoim.domain.club;

import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.dto.ClubCreateDto;
import com.oinzo.somoim.domain.club.dto.ClubRequestDto;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.service.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
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
    public List<Club> readClubListByName(@RequestParam String name) {
        return clubService.readClubListByName(name);
    }

    @GetMapping("/favorite")
    public List<Club> readClubByListFavorite(@RequestParam String favorite, String area){
        return clubService.readClubListByFavorite(favorite,area);
    }

    @GetMapping("/{clubId}")
    public Club readClubById(@PathVariable("clubId") Long clubId, HttpServletResponse response,
                                       @CookieValue(value="count", required=false) Cookie countCookie){
        return clubService.readClubById(clubId,response,countCookie);
    }

    @GetMapping("/random")
    public Page<Club> readClubListByArea(@RequestParam String area, @PageableDefault(size = 10) Pageable pageable){
        return clubService.readClubByArea(area,pageable);
    }

    @GetMapping("/newclub")
    public Page<Club> readClubListByCreateAt(@RequestParam String area, @PageableDefault(size = 10) Pageable pageable){
        return clubService.readClubByCreateAt(area,pageable);
    }


}
