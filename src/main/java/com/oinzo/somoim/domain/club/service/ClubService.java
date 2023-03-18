package com.oinzo.somoim.domain.club.service;

import com.oinzo.somoim.domain.club.dto.ClubCreateDto;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    public Club addClub(Club club){
        return clubRepository.save(Club.from(club));
    }

    public List<Club> readClubByName(String name){
        return clubRepository.findAllByNameContaining(name);
    }

    public List<Club> readClubByFavorite(String favorite, String area){
        return clubRepository.findAllByFavoriteContainingAndAreaContaining(favorite,area);
    }

}
