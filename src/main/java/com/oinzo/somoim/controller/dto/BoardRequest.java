package com.oinzo.somoim.controller.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequest {

    private int clubId;
    private int userId;
    private String category;
    private String title;
    private String content;
    private String imageUrl;
}
