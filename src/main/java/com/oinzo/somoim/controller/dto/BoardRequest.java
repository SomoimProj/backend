package com.oinzo.somoim.controller.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequest {

    private Long clubId;
    private Long userId;
    private String category;
    private String title;
    private String content;
    private String imageUrl;
}
