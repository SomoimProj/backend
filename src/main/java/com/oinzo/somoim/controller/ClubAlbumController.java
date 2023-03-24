package com.oinzo.somoim.controller;

import com.oinzo.somoim.domain.album.dto.AlbumCreateRequest;
import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import com.oinzo.somoim.domain.album.service.ClubAlbumService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/album")
public class ClubAlbumController {
    private final ClubAlbumService clubAlbumService;

    @PostMapping("/{clubId}")
    public ClubAlbum addAlbum(@AuthenticationPrincipal Long userId,
                              @RequestBody @Valid AlbumCreateRequest request, @PathVariable Long clubId){
        return clubAlbumService.addAlbum(request,userId,clubId);
    }

    @GetMapping("/club/{clubId}")
    public List<ClubAlbum> readAllAlbum(@PathVariable Long clubId, Pageable pageable){
        if(pageable.getPageSize()==1) return clubAlbumService.readAllAlbum(clubId);
        return clubAlbumService.readAllAlbumPaging(clubId,pageable);
    }
}
