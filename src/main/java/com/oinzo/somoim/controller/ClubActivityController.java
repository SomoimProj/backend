package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.ClubActivityRequest;
import com.oinzo.somoim.controller.dto.ClubActivityResponse;
import com.oinzo.somoim.domain.clubactivity.service.ClubActivityService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/clubs")
public class ClubActivityController {
    private final ClubActivityService activityService;

    @PostMapping("/{clubId}/activities")
    public SuccessResponse<ClubActivityResponse> addActivity(
            @RequestBody @Valid ClubActivityRequest request,
            @PathVariable Long clubId, @AuthenticationPrincipal Long userId){
        ClubActivityResponse response = activityService.addActivity(request,userId,clubId);
        return ResponseUtil.success(response);
    }

    @GetMapping("{clubId}/activities")
    public SuccessResponse<List<ClubActivityResponse>> readActivity(
            @PathVariable Long clubId){
        List<ClubActivityResponse> responses = activityService.readAllActivity(clubId);
        return ResponseUtil.success(responses);
    }

    @CrossOrigin(origins="*")
    @PatchMapping("/activities/{activityId}")
    public SuccessResponse<ClubActivityResponse> updateActivity(
            @RequestBody ClubActivityRequest request,
            @PathVariable Long activityId, @AuthenticationPrincipal Long userId){
        ClubActivityResponse response = activityService.updateActivity(request,userId,activityId);
        return ResponseUtil.success(response);
    }

    @DeleteMapping("/activities/{activityId}")
    public SuccessResponse<?> deleteActivity(
            @PathVariable Long activityId, @AuthenticationPrincipal Long userId){
        activityService.deleteActivity(userId,activityId);
        return ResponseUtil.success();
    }
}
