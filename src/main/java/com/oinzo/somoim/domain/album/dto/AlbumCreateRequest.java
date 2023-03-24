package com.oinzo.somoim.domain.album.dto;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumCreateRequest {
    @URL
    @NotBlank
    private String imageUrl;
}
