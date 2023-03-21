package com.oinzo.somoim.controller;

import com.oinzo.somoim.controller.dto.BoardRequest;
import com.oinzo.somoim.domain.board.entity.Board;
import com.oinzo.somoim.domain.board.repository.BoardRepository;
import com.oinzo.somoim.domain.board.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping()
    public Board addBoard(@RequestBody @Valid BoardRequest request){
        return boardService.addBoard(request);
    }
}
