package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.AlbumLikeResponse;
import com.oinzo.somoim.domain.albumlike.service.AlbumLikeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class AlbumLikeController {
    private final AlbumLikeService likeService;

    @PostMapping("/albums/{albumId}/likes")
    public SuccessResponse<AlbumLikeResponse> addLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long albumId) {
        AlbumLikeResponse response = likeService.addLike(userId, albumId);
        return ResponseUtil.success(response);
    }

    @DeleteMapping("/albums/{albumId}/likes")
    public SuccessResponse<?> deleteLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long albumId) {
        likeService.deleteLike(userId, albumId);
        return ResponseUtil.success();
    }

    @GetMapping("/albums/{albumId}/likes")
    public SuccessResponse<?> readAllLikes(
            @PathVariable Long albumId) {
        List<AlbumLikeResponse> responses = likeService.readAllLikeList(albumId);
        return ResponseUtil.success(responses);
    }
}