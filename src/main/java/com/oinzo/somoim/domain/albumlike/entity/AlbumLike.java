package com.oinzo.somoim.domain.albumlike.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class AlbumLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ClubAlbum album;

    @NotNull
    private Long userId;

    public static AlbumLike from(Long userId, ClubAlbum album){
        return AlbumLike.builder()
                .userId(userId)
                .album(album)
                .build();
    }

}