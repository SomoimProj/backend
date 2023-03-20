package com.oinzo.somoim;

import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.club.service.ClubService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.description;

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
        Club newClub = new Club(0L,"새로운 클럽","테스트용 클럽","클럽대표사진1","서울",3,0,"SPORTS",0);
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
        Club newClub = new Club(0L,"!?","테스트용 클럽","클럽대표사진1","서울",3,0,"SPORTS",0);
        /* when */
        Object result = clubService.readClubByName(newClub);
        /* then */
        Assertions.assertEquals(ErrorCode.NO_DATA_FOUND,result);
    }

    @Test
    @DisplayName("클럽 관심사로 조회 테스트")
    void readClubByFavorite() {
        /* given */
        Club newClub = new Club(0L,"!?","테스트용 클럽","클럽대표사진1","서울",3,0,"SPORTS",0);
        /* when */;
        Object result = clubService.readClubByFavorite(newClub);
        /* then */
        assertTrue(1 < ((List<Club>) result).size());
    }

    @Test
    @DisplayName("클럽 관심사로 조회 실패 테스트")
    void readClubByFavoriteFaile() {
        /* given */
        Club newClub = new Club(0L,"!?","테스트용 클럽","클럽대표사진1","서울",3,0,"SPORT",0);
        /* when */;
        Object result = clubService.readClubByFavorite(newClub);
        /* then */
        Assertions.assertEquals(result,ErrorCode.WRONG_FAVORITE);
    }

    @Test
    @DisplayName("클럽 랜덤 검색 테스트")
    void readClubByArea() {
        /* given */
        Club newClub = new Club(0L,"!?","테스트용 클럽","클럽대표사진1","서울",3,0,"SPORT",0);
        /* when */;
        Object result = clubService.readClubByArea(newClub);
        /* then */
        assertTrue(1 < ((List<Club>) result).size());
    }
}