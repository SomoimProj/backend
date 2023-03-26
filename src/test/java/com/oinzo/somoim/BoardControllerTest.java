package com.oinzo.somoim;

import com.oinzo.somoim.domain.board.dto.BoardCreateRequest;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import com.oinzo.somoim.domain.board.repository.ClubBoardRepository;
import com.oinzo.somoim.domain.board.service.ClubBoardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BoardControllerTest {

    @Autowired
    ClubBoardService clubBoardService;

    @Autowired
    ClubBoardRepository clubBoardRepository;

    @Test
    @DisplayName("게시판 생성 테스트")
    void addBoard() {
        // given
        BoardCreateRequest newBoard = new BoardCreateRequest("NORMAL","게시판 테스트","URL","자유");
        // when
        ClubBoard board = clubBoardRepository.save(ClubBoard.from(newBoard,1L,7L));
        // then
        Assertions.assertEquals(board.getTitle(),"NORMAL");
    }

    @Test
    @DisplayName("클럽 게시판 전체 조회 테스트")
    void clubBoardList() {
        // given
        // when
        List<ClubBoard> boardList =  clubBoardService.allClubBoardList(1L);
        // then
        assertTrue(boardList.size()>1);

    }

    @Test
    @DisplayName("게시판 상세 조회 테스트")
    void readBoard() {
        // given
        // when
        ClubBoard board = clubBoardService.readBoard(1L);
        // then
        assertEquals(board.getTitle(),"제목");
    }

    @Test
    @DisplayName("게시판 수정 테스트")
    void updateBoard() {
        // given
        BoardCreateRequest newBoard = new BoardCreateRequest("NORMAL","게시판 테스트","URL","자유");
        ClubBoard board = clubBoardRepository.save(ClubBoard.from(newBoard,1L,7L));
        // when
        BoardCreateRequest updateBoard = new BoardCreateRequest("NORMAL","수정된 테스트","URL","자유");
        ClubBoard updateBoard1 =clubBoardService.updateBoard(board.getId(),updateBoard,7L);
        assertEquals(updateBoard1.getTitle(),"NORMAL");
    }
}