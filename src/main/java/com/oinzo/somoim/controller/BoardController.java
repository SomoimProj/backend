package com.oinzo.somoim.controller;

import com.oinzo.somoim.domain.board.dto.BoardCreateRequest;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.service.ClubBoardService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final ClubBoardService boardService;

    @PostMapping("/{clubId}")
    public ClubBoard addBoard(@RequestBody @Valid BoardCreateRequest request,
                              @PathVariable Long clubId, @AuthenticationPrincipal Long userId){
        return boardService.addBoard(request,clubId,userId);
    }

    @GetMapping("/club/{clubId}")
    public List<ClubBoard> clubBoardList(@PathVariable Long clubId, @PageableDefault(size = 10)Pageable pageable){
        if (pageable.getPageSize() == 1) {
            return boardService.allClubBoardList(clubId);
        }
        return boardService.clubBoardList(clubId,pageable);
    }

    @GetMapping("/club/{clubId}/category")
    public List<ClubBoard> boardListByCategory(@PathVariable Long clubId, @RequestParam String category,
                                               @PageableDefault(size = 10)Pageable pageable){
        if (pageable.getPageSize() == 1) {
            return boardService.allClubBoardListByCategory(clubId,category);
        }
        return boardService.clubBoardListByCategory(clubId,category,pageable);
    }

    @GetMapping("/{boardId}")
    public ClubBoard readBoard(@PathVariable Long boardId){
        return boardService.readBoard(boardId);
    }

    @PatchMapping("/{boardId}")
    public ClubBoard updateBoard(@PathVariable Long boardId,BoardCreateRequest request,
                                 @AuthenticationPrincipal Long userId){
        return boardService.updateBoard(boardId,request,userId);
    }

    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId,@AuthenticationPrincipal Long userId){
        boardService.deleteBoard(boardId,userId);
    }

}
