package com.oinzo.somoim.controller.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubActivityRequest {
    @NotBlank
    private String title;
    @NotNull
    @FutureOrPresent
    private LocalDateTime activityTime;
    private String location;
    @PositiveOrZero
    private int fee;
    @NotNull
    @Positive
    private int memberLimit;

}
