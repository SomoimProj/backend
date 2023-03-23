package com.oinzo.somoim.domain.board.dto;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCreateRequest {

    private Long clubId;
    private String category;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @URL
    private String imageUrl;
}
