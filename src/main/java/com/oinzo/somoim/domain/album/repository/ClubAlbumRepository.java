package com.oinzo.somoim.domain.album.repository;

import com.oinzo.somoim.domain.album.entity.ClubAlbum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubAlbumRepository extends JpaRepository<ClubAlbum,Long> {

    Page<ClubAlbum> findAllByClubId(Long clubId, Pageable pageable);
}
