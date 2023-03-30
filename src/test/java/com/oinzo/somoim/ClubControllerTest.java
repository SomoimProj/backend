package com.oinzo.somoim;

import com.oinzo.somoim.controller.dto.ClubCreateRequest;
import com.oinzo.somoim.controller.dto.ClubDetailResponse;
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.club.service.ClubService;
import org.junit.jupiter.api.Assertions;
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
    @DisplayName("클럽 생성 테스트")
    void addClub() {
        /* given */
        ClubCreateRequest newClub = new ClubCreateRequest("새로운 클럽","테스트용 클럽","","서울",3,"GAME");
        /* when */
        ClubDetailResponse clubDetailResponse = clubService.addClub(7L, newClub);
        /* then */
        Assertions.assertEquals("새로운 클럽", clubDetailResponse.getName());
        Assertions.assertEquals(1, clubDetailResponse.getMemberCnt());
    }

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
