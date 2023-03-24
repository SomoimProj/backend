package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.common.type.Category;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCreateRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @URL
    private String imageUrl;
    @NotNull
    private String category;

    public Category getCategoryType() {
        return Category.valueOfOrHandleException(category);
    }
}
