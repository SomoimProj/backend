package com.oinzo.somoim.controller;


import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
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

    @PostMapping
    public SuccessResponse<Club> addClub(@RequestBody @Valid ClubCreateRequest request) {
        Club club = clubService.addClub(request);
        return ResponseUtil.success(club);
    }

    @GetMapping("/search")
    public SuccessResponse<List<Club>> readClubListByName(@RequestParam String name) {
        List<Club> clubs = clubService.readClubListByName(name);
        return ResponseUtil.success(clubs);
    }

    @GetMapping("/favorite")
    public SuccessResponse<List<Club>> readClubByListFavorite(
        @AuthenticationPrincipal Long userId,
        @RequestParam String favorite) {
        List<Club> clubs = clubService.readClubListByFavorite(userId, favorite);
        return ResponseUtil.success(clubs);
    }

    @GetMapping("/{clubId}")
    public SuccessResponse<Club> readClubById(
        @PathVariable("clubId") Long clubId,
        HttpServletResponse response,
        @CookieValue(value="count", required=false) Cookie countCookie) {
        Club club = clubService.readClubById(clubId, response, countCookie);
        return ResponseUtil.success(club);
    }

    @GetMapping("/random")
    public SuccessResponse<List<Club>> readClubListByArea(
        @AuthenticationPrincipal Long userId,
        @PageableDefault(size = 10) Pageable pageable) {
        List<Club> clubs = clubService.readClubListByArea(userId, pageable).getContent();
        return ResponseUtil.success(clubs);
    }

    @GetMapping("/newclub")
    public SuccessResponse<List<Club>> readClubListByCreateAt(
        @AuthenticationPrincipal Long userId,
        @PageableDefault(size = 10) Pageable pageable) {
        List<Club> clubs = clubService.readClubListByCreateAt(userId, pageable).getContent();
        return ResponseUtil.success(clubs);
    }

}
