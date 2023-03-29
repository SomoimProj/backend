package com.oinzo.somoim.controller;

import com.oinzo.somoim.controller.dto.ClubCreateRequest;
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
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
    public SuccessResponse<ClubResponse> addClub(
        @AuthenticationPrincipal Long userId,
        @RequestBody @Valid ClubCreateRequest request) {
        ClubResponse club = clubService.addClub(userId, request);
        return ResponseUtil.success(club);
    }
    
    @GetMapping("/search")
    public SuccessResponse<List<ClubResponse>> readClubListByName(@RequestParam String name) {
        List<ClubResponse> clubs = clubService.readClubListByName(name);
        return ResponseUtil.success(clubs);
    }
    
    @GetMapping("/favorite")
    public SuccessResponse<List<ClubResponse>> readClubByListFavorite(
        @AuthenticationPrincipal Long userId,
        @RequestParam String favorite) {
        List<ClubResponse> clubs = clubService.readClubListByFavorite(userId, favorite);
        return ResponseUtil.success(clubs);
    }

    @GetMapping("/{clubId}")
    public SuccessResponse<ClubResponse> readClubById(
        @PathVariable("clubId") Long clubId,
        HttpServletResponse response,
        @CookieValue(value="count", required=false) Cookie countCookie) {
        ClubResponse club = clubService.readClubById(clubId, response, countCookie);
        return ResponseUtil.success(club);
    }
    
    @GetMapping("/random")
    public SuccessResponse<List<ClubResponse>> readClubListByArea(
        @AuthenticationPrincipal Long userId,
        @PageableDefault(size = 10) Pageable pageable) {
        List<ClubResponse> clubs = clubService.readClubListByArea(userId, pageable);
        return ResponseUtil.success(clubs);
    }

    @GetMapping("/newclub")
    public SuccessResponse<List<ClubResponse>> readClubListByCreateAt(
        @AuthenticationPrincipal Long userId,
        @PageableDefault(size = 10) Pageable pageable) {
        List<ClubResponse> clubs = clubService.readClubListByCreateAt(userId, pageable);
        return ResponseUtil.success(clubs);
    }

}
