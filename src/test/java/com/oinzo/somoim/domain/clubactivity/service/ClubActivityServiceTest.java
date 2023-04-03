package com.oinzo.somoim.domain.clubactivity.service;

import com.oinzo.somoim.controller.dto.ClubActivityRequest;
import com.oinzo.somoim.controller.dto.ClubActivityResponse;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clubactivity.entity.ClubActivity;
import com.oinzo.somoim.domain.clubactivity.repository.ClubActivityRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubActivityServiceTest {
    @InjectMocks
    private ClubActivityService clubActivityService;
    @Mock
    private ClubActivityRepository activityRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClubUserRepository clubUserRepository;
    @Mock
    private ClubRepository clubRepository;

    @Test
    void addActivity() {
        //given
        given(userRepository.existsById(anyLong()))
                .willReturn(true);
        given(clubRepository.existsById(anyLong()))
                .willReturn(true);
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(),anyLong()))
                .willReturn(true);
        when(activityRepository.save(any(ClubActivity.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        ArgumentCaptor<ClubActivity> captor = ArgumentCaptor.forClass(ClubActivity.class);

        // when
        ClubActivityRequest request = ClubActivityRequest.builder()
                .title("액티비티테스트")
                .activityTime(LocalDateTime.now())
                .fee(0)
                .memberLimit(3)
                .build();
        clubActivityService.addActivity(request,1L,1L);

        // then
        verify(activityRepository,times(1)).save(captor.capture());
        assertEquals(1L,captor.getValue().getClubId());
        assertEquals("액티비티테스트",captor.getValue().getTitle());
        assertNull(captor.getValue().getLocation());
        assertEquals(0,captor.getValue().getFee());
        assertEquals(3,captor.getValue().getMemberLimit());
        assertEquals(0,captor.getValue().getMemberCnt());

    }

    @Test
    void readAllActivity() {
        ClubActivity mockActivity1 = ClubActivity.builder()
                .id(1L)
                .clubId(1L)
                .title("액티비티테스트")
                .activityTime(LocalDateTime.now())
                .memberLimit(5)
                .memberCnt(0)
                .build();
        ClubActivity mockActivity2 = ClubActivity.builder()
                .id(2L)
                .clubId(1L)
                .title("액티비티테스트")
                .activityTime(LocalDateTime.now())
                .memberLimit(5)
                .memberCnt(0)
                .build();
        List<ClubActivity> activityList = List.of(
                mockActivity1,
                mockActivity2
        );

        given(clubRepository.existsById(anyLong()))
                .willReturn(true);
        given(activityRepository.findAllByClubId(anyLong()))
                .willReturn(activityList);

        // when
        List<ClubActivityResponse> activities = clubActivityService.readAllActivity(1L);

        // then
        assertEquals(2,activities.size());
        assertEquals("액티비티테스트",activities.get(0).getTitle());
    }

    @Test
    void updateActivity() {
        //given
        ClubActivity mockActivity = ClubActivity.builder()
                .id(1L)
                .clubId(1L)
                .title("액티비티테스트")
                .activityTime(LocalDateTime.now())
                .memberLimit(5)
                .memberCnt(0)
                .build();

        given(userRepository.existsById(anyLong()))
                .willReturn(true);
        given(activityRepository.findById(anyLong()))
                .willReturn(Optional.of(mockActivity));
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(),anyLong()))
                .willReturn(true);
        when(activityRepository.save(any(ClubActivity.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        ArgumentCaptor<ClubActivity> captor = ArgumentCaptor.forClass(ClubActivity.class);

        // when
        ClubActivityRequest request = ClubActivityRequest.builder()
                .title("액티비티테스트")
                .activityTime(LocalDateTime.now())
                .fee(0)
                .memberLimit(3)
                .build();
        clubActivityService.updateActivity(request,1L,1L);

        // then
        verify(activityRepository,times(1)).save(captor.capture());
        assertEquals(1L,captor.getValue().getClubId());
        assertEquals("액티비티테스트",captor.getValue().getTitle());
        assertNull(captor.getValue().getLocation());
        assertEquals(0,captor.getValue().getFee());
        assertEquals(3,captor.getValue().getMemberLimit());
        assertEquals(0,captor.getValue().getMemberCnt());
    }
}