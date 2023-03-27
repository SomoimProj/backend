package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.BoardCreateRequest;
import com.oinzo.somoim.controller.dto.BoardResponse;
import com.oinzo.somoim.domain.board.service.ClubBoardService;
import lombok.AllArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@AllArgsConstructor
public class BoardController {

    private final ClubBoardService boardService;

    @PostMapping("/clubs/{clubId}/boards")
    public SuccessResponse<BoardResponse> addBoard(
        @AuthenticationPrincipal Long userId,
        @RequestBody @Valid BoardCreateRequest request,
        @PathVariable Long clubId) {
        BoardResponse boardResponse = boardService.addBoard(request, clubId, userId);
        return ResponseUtil.success(boardResponse);
    }

    @GetMapping("/clubs/{clubId}/boards")
    public SuccessResponse<List<BoardResponse>> clubBoardList(
        @PathVariable Long clubId,
        @PageableDefault(size = 10) Pageable pageable) {
        List<BoardResponse> boardResponses = boardService.clubBoardList(clubId, pageable);
        return ResponseUtil.success(boardResponses);
    }

    @GetMapping("/clubs/{clubId}/boards/category")
    public SuccessResponse<List<BoardResponse>> boardListByCategory(
        @PathVariable Long clubId,
        @RequestParam String category,
        @PageableDefault(size = 10) Pageable pageable) {
        List<BoardResponse> boardResponses = boardService.clubBoardListByCategory(clubId, category, pageable);
        return ResponseUtil.success(boardResponses);
    }

    @GetMapping("/boards/{boardId}")
    public SuccessResponse<BoardResponse> readBoard(@PathVariable Long boardId){
        BoardResponse boardResponse = boardService.readBoard(boardId);
        return ResponseUtil.success(boardResponse);
    }

    @PatchMapping("/boards/{boardId}")
    public SuccessResponse<BoardResponse> updateBoard(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long boardId,
        @RequestBody BoardCreateRequest request){
        BoardResponse boardResponse = boardService.updateBoard(boardId, request, userId);
        return ResponseUtil.success(boardResponse);
    }

    @DeleteMapping("/boards/{boardId}")
    public SuccessResponse<?> deleteBoard(@PathVariable Long boardId,@AuthenticationPrincipal Long userId){
        boardService.deleteBoard(boardId,userId);
        return ResponseUtil.success();
    }
}
