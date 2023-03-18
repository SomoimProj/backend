package com.oinzo.somoim.domain.club;

import com.oinzo.somoim.common.exception.ErrorResponse;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.service.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/club")
public class ClubController {
    private final ClubService clubService;

    @ResponseBody
    @PostMapping()
    public Club addClub(@RequestBody Club request){
        return clubService.addClub(request);
    }

    @GetMapping("/search")
    public List<Club> readClubByName(@RequestBody String name) {
        return clubService.readClubByName(name);
    }

    @GetMapping("/favorite")
    public List<Club> readClubByFavorite(@RequestBody Club request){
        return clubService.readClubByFavorite(request.getFavorite(),request.getArea());
    }
}
