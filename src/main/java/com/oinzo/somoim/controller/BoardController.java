package com.oinzo.somoim.controller;

import com.oinzo.somoim.domain.board.dto.BoardCreateRequest;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.service.ClubBoardService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public List<ClubBoard> clubBoardList(@PathVariable Long clubId){
        return boardService.clubBoardList(clubId);
    }

    @GetMapping("/{boardId}")
    public ClubBoard readBoard(@PathVariable Long boardId){
        return boardService.readBoard(boardId);
    }

    @PatchMapping("/{boardId}")
    public ClubBoard updateBoard(@PathVariable Long boardId,BoardCreateRequest request
            ,@AuthenticationPrincipal Long userId){
        return boardService.updateBoard(boardId,request,userId);
    }

    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId,@AuthenticationPrincipal Long userId){
        boardService.deleteBoard(boardId,userId);
    }

}
