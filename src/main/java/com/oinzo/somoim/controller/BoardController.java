package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.BoardCreateRequest;
import com.oinzo.somoim.controller.dto.BoardResponse;
import com.oinzo.somoim.domain.board.service.ClubBoardService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/clubs/{clubId}/boards/_page={pageNum}")
    public SuccessResponse<Page<BoardResponse>> clubBoardList(
        @PathVariable Long clubId,
        @PathVariable Integer pageNum) {
        PageRequest pageable = PageRequest.of(pageNum,10);
        Page<BoardResponse> boardResponses = boardService.readClubBoardList(clubId, pageable);
        return ResponseUtil.success(boardResponses);
    }

    @GetMapping("/clubs/{clubId}/boards")
    public SuccessResponse<List<BoardResponse>> allClubBoardList(
            @PathVariable Long clubId) {
        List<BoardResponse> boardResponses = boardService.readClubBoardListAll(clubId);
        return ResponseUtil.success(boardResponses);
    }

    @GetMapping("/clubs/{clubId}/boards/category")
    public SuccessResponse<List<BoardResponse>> allBoardListByCategory(
        @PathVariable Long clubId,
        @RequestParam String category) {
        List<BoardResponse> boardResponses = boardService.readClubBoardListAllByCategory(clubId, category);
        return ResponseUtil.success(boardResponses);
    }

    @GetMapping("/clubs/{clubId}/boards/category/_page={pageNum}")
    public SuccessResponse<Page<BoardResponse>> boardListByCategory(
            @PathVariable Long clubId,
            @PathVariable Integer pageNum,
            @RequestParam String category) {
        PageRequest pageable = PageRequest.of(pageNum,10);
        Page<BoardResponse> boardResponses = boardService.readClubBoardListByCategory(clubId, category, pageable);
        return ResponseUtil.success(boardResponses);
    }

    @GetMapping("/boards/{boardId}")
    public SuccessResponse<BoardResponse> readBoard(
        @PathVariable Long boardId,
        @AuthenticationPrincipal Long userId){
        BoardResponse boardResponse = boardService.readBoard(boardId,userId);
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
