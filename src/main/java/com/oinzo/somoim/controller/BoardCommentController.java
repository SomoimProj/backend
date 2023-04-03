package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.CommentRequest;
import com.oinzo.somoim.controller.dto.CommentResponse;
import com.oinzo.somoim.domain.boardcomment.service.BoardCommentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/boards")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @PostMapping("/{boardId}/comments")
    public SuccessResponse<CommentResponse> addComment(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal Long userId){
        CommentResponse response = boardCommentService.addComment(request,boardId,userId);
        return ResponseUtil.success(response);
    }

    @GetMapping("/comments/{commentId}")
    public SuccessResponse<CommentResponse> readOneComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId){
        CommentResponse response = boardCommentService.readOneComment(commentId,userId);
        return ResponseUtil.success(response);
    }

    @GetMapping("/{boardId}/comments")
    public SuccessResponse<List<CommentResponse>> readAllComment(
            @PathVariable Long boardId,
            @AuthenticationPrincipal Long userId){
        List<CommentResponse> response = boardCommentService.readAllComment(boardId,userId);
        return ResponseUtil.success(response);
    }

    @CrossOrigin(origins="*")
    @PatchMapping("/comments/{commentId}")
    public SuccessResponse<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal Long userId){
        CommentResponse response = boardCommentService.updateComment(request,commentId,userId);
        return ResponseUtil.success(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public SuccessResponse<?> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId){
        boardCommentService.deleteComment(commentId,userId);
        return ResponseUtil.success();
    }
}
