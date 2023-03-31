package com.oinzo.somoim.domain.albumlike.repository;

import com.oinzo.somoim.domain.albumlike.entity.AlbumLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumLikeRepository extends JpaRepository<AlbumLike,Long> {

    boolean existsByAlbumIdAndUserId(Long albumId,Long userId);

    Optional<AlbumLike> findByAlbumIdAndUserId(Long albumId, Long userId);
}