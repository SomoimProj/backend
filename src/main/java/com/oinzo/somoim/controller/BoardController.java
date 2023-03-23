package com.oinzo.somoim.controller;

import com.oinzo.somoim.domain.board.dto.BoardCreateRequest;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.service.ClubBoardService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final ClubBoardService boardService;

    @PostMapping()
    public ClubBoard addBoard(@RequestBody @Valid BoardCreateRequest request, @AuthenticationPrincipal Long userId){
        return boardService.addBoard(request,userId);
    }
}
