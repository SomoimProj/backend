package com.oinzo.somoim.controller;


import com.oinzo.somoim.controller.dto.ClubCreateRequest;
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.service.ClubService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/clubs")
public class ClubController {
    private final ClubService clubService;

    @PostMapping()
    public ClubResponse addClub(@RequestBody @Valid ClubCreateRequest request){
        return clubService.addClub(request);
    }

    @GetMapping("/search")
    public List<ClubResponse> readClubListByName(@RequestParam String name) {
        return clubService.readClubListByName(name);
    }

    @GetMapping("/favorite")
    public List<ClubResponse> readClubByListFavorite(@RequestParam String favorite, String area){
        return clubService.readClubListByFavorite(favorite,area);
    }

    @GetMapping("/{clubId}")
    public ClubResponse readClubById(@PathVariable("clubId") Long clubId, HttpServletResponse response,
                                       @CookieValue(value="count", required=false) Cookie countCookie){
        return clubService.readClubById(clubId,response,countCookie);
    }

    @GetMapping("/random")
    public List<ClubResponse> readClubListByArea(@RequestParam String area, @PageableDefault(size = 10) Pageable pageable){
        if(pageable.getPageSize()==1) return clubService.readAllClubByArea(area);
        return clubService.readClubByArea(area,pageable);
    }

    @GetMapping("/newclub")
    public List<ClubResponse> readClubListByCreateAt(@RequestParam String area, @PageableDefault(size = 10) Pageable pageable){
        if(pageable.getPageSize()==1) return clubService.readAllClubByCreateAt(area);
        return clubService.readClubByCreateAt(area,pageable);
    }

}
