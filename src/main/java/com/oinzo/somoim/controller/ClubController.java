package com.oinzo.somoim.controller;

import com.oinzo.somoim.controller.dto.ClubCreateRequest;
import com.oinzo.somoim.controller.dto.ClubDetailResponse;
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.domain.club.service.ClubService;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public SuccessResponse<ClubDetailResponse> addClub(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ClubCreateRequest request) {
        ClubDetailResponse club = clubService.addClub(userId, request);
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
    public SuccessResponse<ClubDetailResponse> readClubById(
            @PathVariable("clubId") Long clubId,
            HttpServletResponse response,
            @CookieValue(value = "count", required = false) Cookie countCookie) {
        ClubDetailResponse club = clubService.readClubById(clubId, response, countCookie);
        return ResponseUtil.success(club);
    }

    @GetMapping("/random/_page={pageNum}")
    public SuccessResponse<Page<ClubResponse>> readClubListByArea(
            @AuthenticationPrincipal Long userId,
            @PathVariable int pageNum) {
        PageRequest pageable = PageRequest.of(pageNum, 10);
        Page<ClubResponse> clubs = clubService.readClubListByArea(userId, pageable);
        return ResponseUtil.success(clubs);
    }

    @GetMapping("/newclub/_page={pageNum}")
    public SuccessResponse<Page<ClubResponse>> readClubListByCreateAt(
            @AuthenticationPrincipal Long userId,
            @PathVariable int pageNum) {
        PageRequest pageable = PageRequest.of(pageNum, 10);
        Page<ClubResponse> clubs = clubService.readClubListByCreateAt(userId, pageable);
        return ResponseUtil.success(clubs);
    }

    @PatchMapping("/{clubId}")
    public SuccessResponse<ClubDetailResponse> updateClub(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long clubId,
            @RequestBody ClubCreateRequest request) {
        ClubDetailResponse club = clubService.updateClub(request, clubId, userId);
        return ResponseUtil.success(club);
    }

    @GetMapping("/search/favorite")
    public SuccessResponse<List<ClubResponse>> readByFavoriteName(
            @RequestParam String name,
            @RequestParam String favorite) {

        List<ClubResponse> responses = clubService.readByNameFavorite(name,favorite);
        return ResponseUtil.success(responses);
    }
}
