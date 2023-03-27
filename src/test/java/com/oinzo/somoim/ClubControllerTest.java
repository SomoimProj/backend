package com.oinzo.somoim;

import com.oinzo.somoim.domain.club.dto.ClubCreateRequest;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.club.service.ClubService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.security.test.context.support.WithUserDetails;

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
        ClubCreateRequest newClub = new ClubCreateRequest("새로운 클럽","테스트용 클럽","클럽대표사진1","서울",3,"GAME");
        /* when */
        Club club = clubRepository.save(Club.from(newClub));
        /* then */
        Assertions.assertEquals(club.getName(),"새로운 클럽");
        Assertions.assertEquals(club.getMemberCnt(),0);
    }

    @Test
    @DisplayName("클럽 이름으로 조회 테스트")
    void readClubByName() {
        /* given */
        /* when */
        List<Club> result = clubService.readClubListByName("1");
        /* then */
        assertTrue(result.size()>1);
    }

    @Test
    @DisplayName("클럽 관심사로 조회 테스트")
    void readClubByFavorite() {
        /* given */
        /* when */
        List<Club> result = clubService.readClubListByFavorite(7L,"GAME");
        /* then */
        assertTrue(1 < result.size());
    }
}
