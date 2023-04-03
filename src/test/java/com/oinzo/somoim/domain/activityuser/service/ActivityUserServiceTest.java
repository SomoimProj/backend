package com.oinzo.somoim.domain.activityuser.service;

import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.controller.dto.ActivityUserResponse;
import com.oinzo.somoim.domain.activityuser.entity.ActivityUser;
import com.oinzo.somoim.domain.activityuser.repository.ActivityUserRepository;
import com.oinzo.somoim.domain.clubactivity.entity.ClubActivity;
import com.oinzo.somoim.domain.clubactivity.repository.ClubActivityRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityUserServiceTest {
    @InjectMocks
    private ActivityUserService activityUserService;
    @Mock
    private ActivityUserRepository activityUserRepository;
    @Mock
    private ClubActivityRepository activityRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClubUserRepository clubUserRepository;

    @Test
    void addActivityUser() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .name("테스트")
                .birth(LocalDate.parse("2023-03-28"))
                .gender(Gender.MALE)
                .area("서울")
                .introduction("테스트")
                .favorites(List.of(Favorite.GAME))
                .build();
        ClubActivity mockActivity = ClubActivity.builder()
                .id(1L)
                .clubId(1L)
                .title("액티비티테스트")
                .activityTime(LocalDateTime.now())
                .memberLimit(5)
                .memberCnt(0)
                .build();
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(mockUser));
        given(activityRepository.findById(anyLong()))
                .willReturn(Optional.of(mockActivity));
        given(activityUserRepository.existsByUser_IdAndActivityId(anyLong(),anyLong()))
                .willReturn(false);
        given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(),anyLong()))
                .willReturn(true);
        when(activityUserRepository.save(any(ActivityUser.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        ArgumentCaptor<ActivityUser> captor = ArgumentCaptor.forClass(ActivityUser.class);

        // when
        activityUserService.addActivityUser(1L,1L);
        verify(activityUserRepository,times(1)).save(captor.capture());
        assertEquals(1L,captor.getValue().getActivityId());
        assertEquals("테스트",captor.getValue().getUser().getName());
        assertNull(captor.getValue().getUser().getProfileUrl());
        assertEquals(1L,captor.getValue().getActivityId());
    }

    @Test
    void readAllActivityUser() {
        // given
        User mockUser1 = User.builder()
                .id(1L)
                .name("테스트1")
                .birth(LocalDate.parse("2023-03-28"))
                .gender(Gender.MALE)
                .area("서울")
                .introduction("테스트")
                .favorites(List.of(Favorite.GAME))
                .build();
        User mockUser2 = User.builder()
                .id(2L)
                .name("테스트2")
                .birth(LocalDate.parse("2023-03-28"))
                .gender(Gender.MALE)
                .area("서울")
                .introduction("테스트")
                .favorites(List.of(Favorite.GAME))
                .build();
        ActivityUser activityUser1 = ActivityUser.builder()
                .user(mockUser1)
                .activityId(1L)
                .build();
        ActivityUser activityUser2 = ActivityUser.builder()
                .user(mockUser2)
                .activityId(1L)
                .build();
        List<ActivityUser> users = List.of(
                activityUser1,
                activityUser2
        );
        given(activityRepository.existsById(anyLong()))
                .willReturn(true);
        given(activityUserRepository.findAllByActivityId(anyLong()))
                .willReturn(users);

        // when
        List<ActivityUserResponse> userResponses = activityUserService.readAllActivityUser(1L);

        // then
        assertEquals(2,userResponses.size());
        assertEquals("테스트1",userResponses.get(0).getUserName());
        assertEquals("테스트2",userResponses.get(1).getUserName());
    }
}