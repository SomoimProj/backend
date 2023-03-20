package com.oinzo.somoim.domain.club.dto;

import com.oinzo.somoim.common.type.Favorite;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;
    @URL
    private String imageUrl;
    @NotBlank
    private String area;

    @NotBlank
    @Positive
    private int memberLimit;

    @NotBlank
    private String favorite;

    public Favorite getFavoriteType() {
        return Favorite.valueOfOrHandleException(favorite);
    }

}
