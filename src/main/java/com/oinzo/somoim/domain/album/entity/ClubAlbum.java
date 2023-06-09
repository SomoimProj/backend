package com.oinzo.somoim.domain.album.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.controller.dto.AlbumCreateRequest;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ClubAlbum extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @NotNull
    private Long clubId;
    @NotBlank
    private String imageUrl;
    public static ClubAlbum from(AlbumCreateRequest request, User user, Long clubId){
        return ClubAlbum.builder()
                .clubId(clubId)
                .user(user)
                .imageUrl(request.getImageUrl())
                .build();
    }
}