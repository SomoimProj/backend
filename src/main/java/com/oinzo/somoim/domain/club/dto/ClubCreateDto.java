package com.oinzo.somoim.domain.club.dto;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubCreateDto {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String area;
    private int memberLimit;
    private int memberCnt;
    private String favorite;

}
