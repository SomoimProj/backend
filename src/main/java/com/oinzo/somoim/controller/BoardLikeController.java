package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.BoardLikeResponse;
import com.oinzo.somoim.domain.boardlike.service.BoardLikeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BoardLikeController {
    private final BoardLikeService likeService;

    @PostMapping("/boards/{boardId}/likes")
    public SuccessResponse<BoardLikeResponse> addLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long boardId) {
        BoardLikeResponse response = likeService.addLike(userId, boardId);
        return ResponseUtil.success(response);
    }

    @DeleteMapping("/boards/{boardId}/likes")
    public SuccessResponse<?> deleteLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long boardId) {
        likeService.deleteLike(userId, boardId);
        return ResponseUtil.success();
    }

    @GetMapping("/boards/{boardId}/likes")
    public SuccessResponse<?> readAllLike(
            @PathVariable Long boardId) {
        List<BoardLikeResponse> responses = likeService.readAllLike(boardId);
        return ResponseUtil.success(responses);
    }
}