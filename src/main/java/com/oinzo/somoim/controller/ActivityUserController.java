package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.ActivityUserResponse;
import com.oinzo.somoim.domain.activityuser.service.ActivityUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/clubs/activities/")
public class ActivityUserController {
    private final ActivityUserService activityUserService;

    @PostMapping("/{activityId}/users")
    public SuccessResponse<ActivityUserResponse> addActivityUser(
            @PathVariable Long activityId,
            @AuthenticationPrincipal Long userId) {
        ActivityUserResponse response = activityUserService.addActivityUser(userId, activityId);
        return ResponseUtil.success(response);
    }

    @GetMapping("/{activityId}/users")
    public SuccessResponse<List<ActivityUserResponse>> readActivityUserList(
            @PathVariable Long activityId) {
        List<ActivityUserResponse> responses = activityUserService.readAllActivityUser(activityId);
        return ResponseUtil.success(responses);
    }

    @DeleteMapping("/{activityId}/users")
    public SuccessResponse<?> deleteActivityUser(
            @PathVariable Long activityId,
            @AuthenticationPrincipal Long userId) {
        activityUserService.deleteActivityUser(userId,activityId);
        return ResponseUtil.success();
    }
}
