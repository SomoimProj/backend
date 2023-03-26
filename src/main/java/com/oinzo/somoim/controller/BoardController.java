package com.oinzo.somoim.controller;

import com.oinzo.somoim.controller.dto.BoardCreateRequest;
import com.oinzo.somoim.controller.dto.BoardResponse;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
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
    public BoardResponse addBoard(@RequestBody @Valid BoardCreateRequest request,
                                  @PathVariable Long clubId, @AuthenticationPrincipal Long userId){
        return boardService.addBoard(request,clubId,userId);
    }

    @GetMapping("/clubs/{clubId}/boards")
    public List<BoardResponse> clubBoardList(@PathVariable Long clubId, @PageableDefault(size = 10)Pageable pageable){
        if (pageable.getPageSize() == 1) {
            return boardService.allClubBoardList(clubId);
        }
        return boardService.clubBoardList(clubId,pageable);
    }

    @GetMapping("/clubs/{clubId}/boards/category")
    public List<BoardResponse> boardListByCategory(@PathVariable Long clubId, @RequestParam String category,
                                               @PageableDefault(size = 10)Pageable pageable){
        if (pageable.getPageSize() == 1) {
            return boardService.allClubBoardListByCategory(clubId,category);
        }
        return boardService.clubBoardListByCategory(clubId,category,pageable);
    }

    @GetMapping("/boards/{boardId}")
    public BoardResponse readBoard(@PathVariable Long boardId){
        return boardService.readBoard(boardId);
    }

    @PatchMapping("/boards/{boardId}")
    public BoardResponse updateBoard(@PathVariable Long boardId,BoardCreateRequest request,
                                 @AuthenticationPrincipal Long userId){
        return boardService.updateBoard(boardId,request,userId);
    }

    @DeleteMapping("/boards/{boardId}")
    public void deleteBoard(@PathVariable Long boardId,@AuthenticationPrincipal Long userId){
        boardService.deleteBoard(boardId,userId);
    }
}
