package com.oinzo.somoim.domain.albumlike.repository;

import com.oinzo.somoim.domain.albumlike.entity.AlbumLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumLikeRepository extends JpaRepository<AlbumLike, Long> {

    Boolean existsByAlbum_IdAndUser(Long albumId, Long userId);

    Optional<AlbumLike> findByAlbum_IdAndUser(Long albumId, Long userId);

    List<AlbumLike> findAllByAlbum_Id(Long albumId);

    int countAllByAlbum_Id(Long albumId);
}