package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.controller.dto.AlbumCreateRequest;
import com.oinzo.somoim.controller.dto.AlbumResponse;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.domain.album.service.ClubAlbumService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/clubs")
public class ClubAlbumController {
    private final ClubAlbumService clubAlbumService;

    @PostMapping("{clubId}/albums")
    public SuccessResponse<AlbumResponse> addAlbum(@AuthenticationPrincipal Long userId,
                                                   @RequestBody @Valid AlbumCreateRequest request, @PathVariable Long clubId) {
        AlbumResponse albumResponse = clubAlbumService.addAlbum(request, userId, clubId);
        return ResponseUtil.success(albumResponse);
    }

    @GetMapping("{clubId}/albums")
    public SuccessResponse<List<AlbumResponse>> readAllAlbum(@PathVariable Long clubId) {
        List<AlbumResponse> albumResponses = clubAlbumService.readAllAlbumList(clubId);
        return ResponseUtil.success(albumResponses);
    }

    @GetMapping("{clubId}/albums/_page={pageNum}")
    public SuccessResponse<Page<AlbumResponse>> readAllAlbumByPaging(@PathVariable Long clubId,
                                                             @PathVariable int pageNum) {
        PageRequest pageable = PageRequest.of(pageNum,10);
        Page<AlbumResponse> albumResponses = clubAlbumService.readAllAlbum(clubId, pageable);
        return ResponseUtil.success(albumResponses);
    }

    @GetMapping("/albums/{albumId}")
    public SuccessResponse<AlbumResponse> readAlbum(@PathVariable Long albumId,
                                                    @AuthenticationPrincipal Long userId) {
        AlbumResponse albumResponse = clubAlbumService.readOneAlbum(albumId, userId);
        return ResponseUtil.success(albumResponse);
    }

    @DeleteMapping("/albums/{albumId}")
    public SuccessResponse<?> deleteAlbum(@PathVariable Long albumId,
                                          @AuthenticationPrincipal Long userId) {
        clubAlbumService.deleteAlbum(albumId, userId);
        return ResponseUtil.success();
    }
}