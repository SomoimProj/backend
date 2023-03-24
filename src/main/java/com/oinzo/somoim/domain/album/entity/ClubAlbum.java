package com.oinzo.somoim.domain.album.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.domain.album.dto.AlbumCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ClubAlbum extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Long clubId;
    @NotBlank
    private String imageUrl;
    public static ClubAlbum from(AlbumCreateRequest request, Long userId, Long clubId){
        return ClubAlbum.builder()
                .clubId(clubId)
                .userId(userId)
                .imageUrl(request.getImageUrl())
                .build();
    }
}