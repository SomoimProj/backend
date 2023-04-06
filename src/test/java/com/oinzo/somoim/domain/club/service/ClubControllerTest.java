package com.oinzo.somoim.domain.club.service;

import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClubControllerTest {

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    ClubService clubService;

    @Test
    @DisplayName("클럽 이름으로 조회 테스트")
    void readClubByName() {
        /* given */
        /* when */
        List<ClubResponse> result = clubService.readClubListByName("1");
        /* then */
        assertTrue(result.size() > 1);
    }

    @Test
    @DisplayName("클럽 관심사로 조회 테스트")
    void readClubByFavorite() {
        /* given */
        /* when */
        List<ClubResponse> result = clubService.readClubListByFavorite(7L,"GAME");
        /* then */
        assertTrue(result.size() > 1);
    }
}
