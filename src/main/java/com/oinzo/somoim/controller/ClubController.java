package com.oinzo.somoim.controller;


import com.oinzo.somoim.domain.club.dto.ClubCreateRequest;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.service.ClubService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Club addClub(@RequestBody @Valid ClubCreateRequest request){
        return clubService.addClub(request);
    }

    @GetMapping("/search")
    public List<Club> readClubListByName(@RequestParam String name) {
        return clubService.readClubListByName(name);
    }

    @GetMapping("/favorite")
    public List<Club> readClubByListFavorite(@RequestParam String favorite, String area){
        return clubService.readClubListByFavorite(favorite, area);
    }

    @GetMapping("/{clubId}")
    public Club readClubById(
        @PathVariable("clubId") Long clubId,
        HttpServletResponse response,
        @CookieValue(value="count", required=false) Cookie countCookie){
        return clubService.readClubById(clubId, response, countCookie);
    }

    @GetMapping("/random")
    public List<Club> readClubListByArea(
        @AuthenticationPrincipal Long userId,
        @PageableDefault(size = 10) Pageable pageable){
        if (pageable.getPageSize() == 1) {
            return clubService.readAllClubListByArea(userId).getContent();
        }
        return clubService.readClubListByArea(userId, pageable).getContent();
    }

    @GetMapping("/newclub")
    public List<Club> readClubListByCreateAt(
        @AuthenticationPrincipal Long userId,
        @PageableDefault(size = 10) Pageable pageable){
        if (pageable.getPageSize() == 1) {
            return clubService.readAllClubListByCreateAt(userId).getContent();
        }
        return clubService.readClubListByCreateAt(userId, pageable).getContent();
    }

}
