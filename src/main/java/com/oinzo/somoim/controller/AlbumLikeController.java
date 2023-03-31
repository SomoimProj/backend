package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.AlbumLikeResponse;
import com.oinzo.somoim.domain.albumlike.service.AlbumLikeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AlbumLikeController {
    private final AlbumLikeService likeService;

    @PostMapping("/albums/{albumId}/likes")
    public SuccessResponse<AlbumLikeResponse> addLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long albumId){
        AlbumLikeResponse response = likeService.addLike(userId,albumId);
        return ResponseUtil.success(response);
    }

    @DeleteMapping("/albums/{albumId}/likes")
    public SuccessResponse<?> deleteLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long albumId){
        likeService.deleteLike(userId,albumId);
        return ResponseUtil.success();
    }
}